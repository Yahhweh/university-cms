package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.service.GroupService;
import placeholder.organisation.unicms.service.LecturerService;
import placeholder.organisation.unicms.service.StudentService;
import placeholder.organisation.unicms.service.SubjectService;
import placeholder.organisation.unicms.service.dto.response.AddressResponseDTO;
import placeholder.organisation.unicms.service.mapper.AddressMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @MockitoBean
    private LecturerService lecturerService;

    @MockitoBean
    private GroupService groupService;

    @MockitoBean
    private SubjectService subjectService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private AddressMapper addressMapper;


    @Test
    void showAdminPanel_shouldReturnAdminView() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"));
    }


    @Test
    void showChangeRoleForm_shouldReturnChangeRoleView_withStudentsAndLecturers() throws Exception {
        List<Student> students = List.of(getStudent());
        List<Lecturer> lecturers = List.of(getLecturer());

        when(studentService.findAllStudents()).thenReturn(students);
        when(lecturerService.findAllLecturers()).thenReturn(lecturers);

        mockMvc.perform(get("/admin/change-role"))
                .andExpect(status().isOk())
                .andExpect(view().name("change-role"))
                .andExpect(model().attribute("students", students))
                .andExpect(model().attribute("lecturers", lecturers));
    }


    @Test
    void changeUserRole_shouldRedirectAndUpdateStudent_whenStudentFound() throws Exception {
        Student student = getStudent();
        when(studentService.findStudent(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(post("/admin/change-role")
                        .param("id", "1")
                        .param("role", "ROLE_ADMIN")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/change-role"));

        verify(studentService).createStudent(student);
    }

    @Test
    void changeUserRole_shouldRedirectAndUpdateLecturer_whenStudentNotFound() throws Exception {
        Lecturer lecturer = getLecturer();
        when(studentService.findStudent(2L)).thenReturn(Optional.empty());
        when(lecturerService.findLecturer(2L)).thenReturn(Optional.of(lecturer));

        mockMvc.perform(post("/admin/change-role")
                        .param("id", "2")
                        .param("role", "ROLE_STUDENT")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/change-role"));

        verify(lecturerService).createLecturer(lecturer);
    }


    @Test
    void showAddStudentForm_shouldReturnAddStudentView_withGroups() throws Exception {
        List<Group> groups = List.of(new Group(1L, "AB-11"));
        when(groupService.findAllGroups()).thenReturn(groups);

        mockMvc.perform(get("/admin/add-student"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-student"))
                .andExpect(model().attribute("groups", groups));
    }


    @Test
    void addStudent_shouldRedirectToAddStudent_whenStudentCreated() throws Exception {
        when(passwordEncoder.encode("secret")).thenReturn("encoded");
        when(addressMapper.toEntity((AddressResponseDTO) any())).thenReturn(new Address());

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
                .andExpect(redirectedUrl("/admin/add-student"));

        verify(studentService).createStudent((Student) any());
    }


    @Test
    void showAddLecturerForm_shouldReturnAddLecturerView_withSubjects() throws Exception {
        List<Subject> subjects = List.of(new Subject(1L, "Math"));
        when(subjectService.findAllSubjects()).thenReturn(subjects);

        mockMvc.perform(get("/admin/add-lecturer"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-lecturer"))
                .andExpect(model().attribute("subjects", subjects));
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
                .andExpect(redirectedUrl("/admin/add-lecturer"));

        verify(lecturerService).createLecturer((Lecturer) any());
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
