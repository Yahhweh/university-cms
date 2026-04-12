package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.service.*;
import placeholder.organisation.unicms.service.mapper.AddressMapper;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(username = "user", roles = {"ADMIN", "STAFF"})
@WebMvcTest
public class SharedControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private StudentService studentService;
    @MockitoBean
    private LecturerService lecturerService;
    @MockitoBean
    private CourseService courseService;
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
    @MockitoBean
    private DurationService durationService;
    @MockitoBean
    private RoomService roomService;
    @MockitoBean
    private RoomTypeService roomTypeService;

    @Test
    void updateLecturerSubject_shouldRedirect_whenSubjectsUpdated() throws Exception {
        Long subjectId = 1L;
        Long lecturerId = 1L;
        mockMvc.perform(post("/users/update-lecturer-subjects")
                .param("lecturerId", "1")
                .param("subjectIds", "1")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/users/update-lecturer-subjects?lecturerId=1"));

        verify(lecturerService).updateLecturerSubjects(List.of(subjectId), lecturerId);
    }



    private Student getStudent() {
        Student student = new Student();
        student.setId(1L);
        student.getRoles().add(Role.STUDENT);
        return student;
    }

    private Lecturer getLecturer() {
        Lecturer lecturer = new Lecturer();
        lecturer.setId(2L);
        lecturer.getRoles().add(Role.LECTURER);
        return lecturer;
    }

    private Course getCourse(){
        return new Course(1L, "SE", List.of(new Subject()));
    }
}
