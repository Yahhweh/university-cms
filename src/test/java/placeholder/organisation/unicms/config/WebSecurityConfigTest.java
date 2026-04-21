package placeholder.organisation.unicms.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.controller.AdminController;
import placeholder.organisation.unicms.controller.AuthenticationController;
import placeholder.organisation.unicms.controller.LecturerController;
import placeholder.organisation.unicms.controller.StudentController;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.service.*;
import placeholder.organisation.unicms.service.mapper.AddressMapper;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {AdminController.class, AuthenticationController.class, LecturerController.class, StudentController.class})
@Import(WebSecurityConfig.class)
class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @MockitoBean
    private LecturerService lecturerService;

    @MockitoBean
    LessonService lessonService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private GroupService groupService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private SubjectService subjectService;

    @MockitoBean
    private AddressMapper addressMapper;

    @MockitoBean
    private CourseService courseService;

    @Test
    @WithMockUser(username = "user", roles = {"STUDENT", "ADMIN", "STAFF"})
    void filterChain_shouldReturnOk_whenUserHasAllRolesExceptLecturer() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk());
    }

    @Test
    void filterChain_shouldReturnOk_whenAnonymousUserAccessesLogin() throws Exception {
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = "STUDENT")
    void filterChain_shouldRedirectToAccessDenied_whenStudentAccessesAdmin() throws Exception {
        mockMvc.perform(get("/admin"))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/access-denied"));
    }

    @Test
    @WithMockUser(username = "user", roles = "STUDENT")
    void filterChain_shouldRedirectToAccessDenied_whenStudentAccessesLecturerProfile() throws Exception {
        mockMvc.perform(get("/lecturers/profile"))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/access-denied"));
    }

    @Test
    @WithMockUser(username = "user", roles = "LECTURER")
    void filterChain_shouldRedirectToAccessDenied_whenLecturerAccessesStudentProfile() throws Exception {
        mockMvc.perform(get("/students/profile"))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/access-denied"));
    }

    @Test
    void filterChain_shouldReturnOk_whenStudentAccessesOwnProfile() throws Exception {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername("kirill.kovalenko@student.university.com")
            .password("123").roles("STUDENT").build();

        when(customUserDetailsService.loadUserByUsername("kirill.kovalenko@student.university.com")).thenReturn(userDetails);

        when(studentService.findByEmail(userDetails.getUsername())).thenReturn(getStudent());

        mockMvc.perform(get("/students/profile").with(authentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
            )))
            .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void filterChain_shouldRedirectToLogin_whenAnonymousUserAccessesRoot() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void filterChain_shouldReturnOk_whenAdminAccessesAdmin() throws Exception {
        mockMvc.perform(get("/admin"))
            .andExpect(status().isOk());
    }

    Student getStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setName("Kirill");
        student.setSureName("Kovalenko");
        student.setPassword("123");
        student.setDegree(Degree.Bachelor);
        student.getRoles().add(Role.STUDENT);
        student.setDateOfBirth(LocalDate.of(2000, 10, 20));
        student.setEmail("kirill.kovalenko@student.university.com");
        student.setGender(GenderType.Male);
        student.setGroup(new Group(1L, "AB-11", getCourse(), getMentor(), "info"));
        student.setAddress(new Address(1L, "kyiv", "tarasa shevchenka", "Ukraine", "1111", "1012", "1212"));
        return student;
    }

    private Course getCourse() {
        return new Course(1L, "SE", List.of(new Subject()));
    }

    private Student getMentor() {
        Student student = new Student();
        student.setId(1L);
        student.setName("Bob");
        student.setSureName("SureName");
        student.setDegree(Degree.Bachelor);
        student.getRoles().add(Role.MENTOR);
        return student;
    }
}