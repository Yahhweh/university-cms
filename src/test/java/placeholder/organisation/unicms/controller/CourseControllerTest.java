package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Course;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.service.CourseService;
import placeholder.organisation.unicms.service.LecturerService;
import placeholder.organisation.unicms.service.SubjectService;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CourseService courseService;

    @MockitoBean
    SubjectService subjectService;

    @MockitoBean
    LecturerService lecturerService;

    @Test
    @WithMockUser(username = "darius.zabuluonis@lecturer.university.com", roles = "LECTURER")
    void getLecturerCourses_ShouldReturnViewName_whenEverythingIsCorrect() throws Exception {
        Lecturer lecturer = getLecturer();
        List<Course> courses = List.of(getCourse());

        when(lecturerService.findByEmail("darius.zabuluonis@lecturer.university.com")).thenReturn(lecturer);
        when(courseService.findCoursesRelatedToLecturer(lecturer.getId())).thenReturn(courses);

        mockMvc.perform(get("/my-courses")
                .param("lecturerId", String.valueOf(lecturer.getId())))
            .andExpect(status().isOk())
            .andExpect(view().name("lecturer-courses"))
            .andExpect(model().attribute("courses", courses));

        verify(courseService).findCoursesRelatedToLecturer(lecturer.getId());
        verify(lecturerService).findByEmail("darius.zabuluonis@lecturer.university.com");
    }

    private Lecturer getLecturer() {
        Lecturer lecturer = new Lecturer();
        lecturer.setId(1L);
        lecturer.setName("Darius");
        lecturer.setSureName("Zabuluonis");
        return lecturer;
    }

    private Course getCourse() {
        Course course = new Course();
        course.setId(1L);
        course.setName("Programming");
        return course;
    }
}
