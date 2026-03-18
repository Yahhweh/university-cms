package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.service.*;
import placeholder.organisation.unicms.service.dto.request.AddressRequestDTO;
import placeholder.organisation.unicms.service.dto.request.FilterRequestDTO;
import placeholder.organisation.unicms.service.dto.request.LecturerRequestDTO;
import placeholder.organisation.unicms.service.dto.request.StudentRequestDTO;
import placeholder.organisation.unicms.service.dto.request.UserRequestDTO;
import placeholder.organisation.unicms.service.dto.response.AddressResponseDTO;
import placeholder.organisation.unicms.service.mapper.AddressMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@WithMockUser(username = "user", roles = {"ADMIN"})
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private StudentService studentService;
    @MockitoBean
    private LecturerService lecturerService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private GroupService groupService;
    @MockitoBean
    private SubjectService subjectService;
    @MockitoBean
    private PasswordEncoder passwordEncoder;
    @MockitoBean
    private AddressMapper addressMapper;

    @Test
    void showAdminPanel_shouldReturnAdminView_whenAdmin() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"));
    }


    @Test
    void getUsers_shouldReturnChangeRoleView_withStudentsAndLecturers() throws Exception {
        List<Student> students = List.of(getStudent());
        List<Lecturer> lecturers = List.of(getLecturer());

        List<User> people = new ArrayList<>();
        people.addAll(students);
        people.addAll(lecturers);

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(people, pageable, 2);

        when(userService.findAllFiltered(any(FilterRequestDTO.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/admin/users")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("persons"));
    }


    @Test
    void changeUserRole_shouldReturnSuccessView_whenRoleChanged() throws Exception {
        mockMvc.perform(post("/admin/change-role")
                .param("id", "1")
                .param("changeRole", "ROLE_ADMIN")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("/admin/users*"));

        verify(userService).changeRole(1L, Role.ADMIN);
    }

    @Test
    void changeUserRole_shouldReturnSuccessView_whenRoleChangedToStudent() throws Exception {
        mockMvc.perform(post("/admin/change-role")
                        .param("id", "1")
                        .param("changeRole", "ROLE_STUDENT")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/admin/users*"));

        verify(userService).changeRole(1L, Role.STUDENT);
    }

    @Test
    void showAddUserForm_shouldReturnAddUserView_withGroupsAndSubjects() throws Exception {
        List<Group> groups = List.of(new Group(1L, "AB-11"));
        List<Subject> subjects = List.of(new Subject(1L, "Math"));
        when(groupService.findAllGroups()).thenReturn(groups);
        when(subjectService.findAllSubjects()).thenReturn(subjects);

        mockMvc.perform(get("/admin/add-user"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-user"))
                .andExpect(model().attribute("groups", groups))
                .andExpect(model().attribute("subjects", subjects));
    }


    @Test
    void addStudent_shouldRedirectToAddStudent_whenStudentCreated() throws Exception {
        when(passwordEncoder.encode("secret")).thenReturn("encoded");
        when(addressMapper.toEntity((AddressRequestDTO) any())).thenReturn(new Address());

        mockMvc.perform(post("/admin/add-student")
                        .param("name", "Ivan")
                        .param("sureName", "Petrov")
                        .param("password", "secret")
                        .param("gender", "Male")
                        .param("dateOfBirth", "2000-05-15")
                        .param("degree", "Bachelor")
                        .param("city", "Kyiv")
                        .param("street", "Khreshchatyk")
                        .param("phoneNumber", "0991234567")
                        .param("houseNumber", "10")
                        .param("postalCode", "01001")
                        .param("country", "Ukraine")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/add-user"));

        verify(studentService).createStudent((any(StudentRequestDTO.class)));
    }


    @Test
    void addLecturer_shouldRedirectToAddLecturer_whenLecturerCreated() throws Exception {
        when(passwordEncoder.encode("secret")).thenReturn("encoded");
        when(addressMapper.toEntity((AddressResponseDTO) any())).thenReturn(new Address());

        mockMvc.perform(post("/admin/add-lecturer")
                        .param("name", "Anna")
                        .param("sureName", "Kovalenko")
                        .param("password", "secret")
                        .param("gender", "Female")
                        .param("dateOfBirth", "1985-03-20")
                        .param("city", "Lviv")
                        .param("street", "Svobody")
                        .param("phoneNumber", "0671234567")
                        .param("houseNumber", "5")
                        .param("postalCode", "79000")
                        .param("country", "Ukraine")
                        .param("salary", "55000")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/add-user"));

        verify(lecturerService).createLecturer((any(LecturerRequestDTO.class)));
    }

    @Test
    void getListUsers_shouldReturnFilteredPage_whenFilterByNameAndRole() throws Exception {
        Student student = getStudent();
        student.setName("John");
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> people = new PageImpl<>(List.of(student), pageable, 1);
        FilterRequestDTO filterRequestDTO = new FilterRequestDTO();
        filterRequestDTO.setRole(Role.STUDENT);
        filterRequestDTO.setName("oh");

        when(userService.findAllFiltered(any(FilterRequestDTO.class), any(Pageable.class))).thenReturn(people);
        mockMvc.perform(get("/admin/users")
            .param("name","oh")
            .param("role", "STUDENT"))
            .andExpect(status().isOk())
            .andExpect(view().name("users"))
            .andExpect(model().attribute( "page", people))
            .andExpect(model().attribute("persons", people.getContent()))
            .andExpect(model().attribute("url", "admin/users"))
            .andExpect(model().attribute("filters", filterRequestDTO));
    }

    @Test
    void deleteUser_shouldReturnSuccessView_whenPersonDeleted() throws Exception {
        mockMvc.perform(post("/admin/delete-person").param("id", "1").with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("/admin/users*"));
        verify(userService).deletePerson(1L);
    }

    @Test
    void getAssignSubjectForm_shouldReturnAssignSubjectView_withLecturersAndSubjects() throws Exception {
        mockMvc.perform(get("/admin/assign-subject"))
            .andExpect(view().name("assign-subject"))
            .andExpect(status().isOk());
    }

    @Test
    void assignSubjectForm_shouldReturnSuccess_whenSubjectAssigned() throws Exception {
        mockMvc.perform(post("/admin/assign-subject")
                .param("lecturerId", "1")
                .param("subjectId", "1")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/assign-subject"));

        verify(lecturerService).assignSubjectToLecturer(1L, 1L);
    }

    @Test
    void removeSubject_shouldReturnSuccess_whenSubjectRemoved() throws Exception {
        mockMvc.perform(post("/admin/remove-subject")
                .param("lecturerId", "1")
                .param("subjectId", "1")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/remove-subject"));

        verify(lecturerService).removeSubjectFromLecturer(1L, 1L);
    }

    @Test
    void getRemoveSubjectForm_shouldReturnSelectedLecturer_whenLecturerIdProvided() throws Exception {
        Lecturer lecturer = getLecturer();
        when(lecturerService.findAllLecturers()).thenReturn(List.of(lecturer));
        when(lecturerService.findLecturer(2L)).thenReturn(Optional.of(lecturer));

        mockMvc.perform(get("/admin/remove-subject").param("lecturerId", "2"))
            .andExpect(status().isOk())
            .andExpect(view().name("remove-subject"))
            .andExpect(model().attributeExists("selectedLecturer"))
            .andExpect(model().attributeExists("subjects"));
    }

    private Student getStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setRole(Role.STUDENT);
        return student;
    }

    private Lecturer getLecturer() {
        Lecturer lecturer = new Lecturer();
        lecturer.setId(2L);
        lecturer.setRole(Role.LECTURER);
        return lecturer;
    }
}
