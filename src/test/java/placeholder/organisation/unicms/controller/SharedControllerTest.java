package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
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
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

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
    void getRemoveSubjectForm_shouldReturnSelectedLecturer_whenLecturerIdProvided() throws Exception {
        Lecturer lecturer = getLecturer();
        when(lecturerService.findAllLecturers()).thenReturn(List.of(lecturer));
        when(lecturerService.findLecturer(2L)).thenReturn(Optional.of(lecturer));

        mockMvc.perform(get("/users/remove-subject").param("lecturerId", "2"))
            .andExpect(status().isOk())
            .andExpect(view().name("remove-subject"))
            .andExpect(model().attributeExists("selectedLecturer"))
            .andExpect(model().attributeExists("subjects"));
    }

    @Test
    void removeSubject_shouldReturnSuccess_whenSubjectRemoved() throws Exception {
        Long subjectId = 1L;
        Long lecturerId = 1L;
        mockMvc.perform(post("/users/remove-subject")
                .param("lecturerId", "1")
                .param("subjectsId", "1")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/users/remove-subject"));

        verify(lecturerService).removeSubjectFromLecturer(subjectId, lecturerId);
    }

    @Test
    void getAssignSubjectForm_shouldReturnAssignSubjectView_withLecturersAndSubjects() throws Exception {
        mockMvc.perform(get("/users/assign-subject"))
            .andExpect(view().name("assign-subject"))
            .andExpect(status().isOk());
    }

    @Test
    void assignSubjectForm_shouldReturnSuccess_whenSubjectAssigned() throws Exception {
        Long subjectId = 1L;
        Long lecturerId = 1L;
        mockMvc.perform(post("/users/assign-subject")
                .param("lecturerId", "1")
                .param("subjectsId", "1")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/users/assign-subject"));

        verify(lecturerService).assignSubjectToLecturer(subjectId, lecturerId);
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

    private Course getCourse(){
        return new Course(1L, "SE", List.of(new Subject()));
    }
}
