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
import placeholder.organisation.unicms.service.dto.request.*;
import placeholder.organisation.unicms.service.dto.request.filter.UserFilterRequestDTO;
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

@WebMvcTest({AdminController.class, GlobalExceptionHandler.class})
@WithMockUser(username = "user", roles = {"ADMIN"})
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private StudentService studentService;
    @MockitoBean
    private LecturerService lecturerService;
    @MockitoBean
    private LessonService lessonService;
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

        when(userService.findAllFiltered(any(UserFilterRequestDTO.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/admin/users")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(view().name("users"))
            .andExpect(model().attributeExists("users"));
    }


    @Test
    void changeUserRole_shouldReturnSuccessView_whenRoleChanged() throws Exception {
        mockMvc.perform(post("/admin/change-role")
                .param("id", "1")
                .param("newRole", "ROLE_ADMIN")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("/admin/users*"));

        verify(userService).changeRole(1L, Role.ADMIN);
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
        mockMvc.perform(post("/admin/add-student")
                .param("name", "Ivan")
                .param("sureName", "Petrov")
                .param("password", "secret")
                .param("gender", "Male")
                .param("dateOfBirth", "2000-05-15")
                .param("email", "ivan.petrov@university.com")
                .param("degree", "Bachelor")
                .param("address.city", "Kyiv")
                .param("address.street", "Khreshchatyk")
                .param("address.phoneNumber", "0991234567")
                .param("address.houseNumber", "10")
                .param("address.postalCode", "01001")
                .param("address.country", "Ukraine")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/add-user"));

        verify(studentService).createStudent((any(StudentRequestDTO.class)));
    }


    @Test
    void addLecturer_shouldRedirectToAddLecturer_whenLecturerCreated() throws Exception {
        mockMvc.perform(post("/admin/add-lecturer")
                .param("name", "Anna")
                .param("sureName", "Kovalenko")
                .param("password", "secret")
                .param("gender", "Female")
                .param("dateOfBirth", "1985-03-20")
                .param("email", "anna.kovalenko@university.com")
                .param("address.city", "Lviv")
                .param("address.street", "Svobody")
                .param("address.phoneNumber", "0671234567")
                .param("address.houseNumber", "5")
                .param("address.postalCode", "79000")
                .param("address.country", "Ukraine")
                .param("salary", "55000")
                .param("studySubjectIds", "1")
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
        UserFilterRequestDTO userFilterRequestDTO = new UserFilterRequestDTO();
        userFilterRequestDTO.setRole(Role.STUDENT);
        userFilterRequestDTO.setName("oh");

        when(userService.findAllFiltered(any(UserFilterRequestDTO.class), any(Pageable.class))).thenReturn(people);
        mockMvc.perform(get("/admin/users")
                .param("name", "oh")
                .param("role", "STUDENT"))
            .andExpect(status().isOk())
            .andExpect(view().name("users"))
            .andExpect(model().attribute("page", people))
            .andExpect(model().attribute("users", people.getContent()))
            .andExpect(model().attribute("url", "admin/users"))
            .andExpect(model().attribute("filters", userFilterRequestDTO));
    }

    @Test
    void deleteUser_shouldReturnSuccessView_whenPersonDeleted() throws Exception {
        mockMvc.perform(post("/admin/delete-user").param("id", "1").with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("/admin/users*"));
        verify(userService).deleteUser(1L);
    }

    @Test
    void getAssignSubjectForm_shouldReturnAssignSubjectView_withLecturersAndSubjects() throws Exception {
        mockMvc.perform(get("/admin/assign-subject"))
            .andExpect(view().name("assign-subject"))
            .andExpect(status().isOk());
    }

    @Test
    void assignSubjectForm_shouldReturnSuccess_whenSubjectAssigned() throws Exception {
        Long subjectId = 1L;
        Long lecturerId = 1L;
        mockMvc.perform(post("/admin/assign-subject")
                .param("lecturerId", "1")
                .param("subjectsId", "1")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/assign-subject"));

        verify(lecturerService).assignSubjectToLecturer(subjectId, lecturerId);
    }

    @Test
    void removeSubject_shouldReturnSuccess_whenSubjectRemoved() throws Exception {
        Long subjectId = 1L;
        Long lecturerId = 1L;
        mockMvc.perform(post("/admin/remove-subject")
                .param("lecturerId", "1")
                .param("subjectsId", "1")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/remove-subject"));

        verify(lecturerService).removeSubjectFromLecturer(subjectId, lecturerId);
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

    @Test
    void addUser_shouldRedirect_whenUserCreated() throws Exception {
        mockMvc.perform(post("/admin/add-user")
                .param("name", "Ab")
                .param("sureName", "Fkjdkfjdkjfdf")
                .param("gender", "Male")
                .param("password", "secret")

                .param("dateOfBirth", "2000-05-15")
                .param("email", "fdfkdkfj.dfd@gmail.com")
                .param("address.city", "Kyiv")
                .param("address.street", "Obolon")
                .param("address.phoneNumber", "+380454545454")
                .param("address.country", "Ukraine")
                .param("address.houseNumber", "4AA")
                .param("address.postalCode", "01001")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("successMessage", "User has been successfully created"));

        verify(userService).createUser(any(UserRequestDTO.class));
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
