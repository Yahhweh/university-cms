package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.entity.Lesson;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.service.LecturerService;
import placeholder.organisation.unicms.service.LessonService;
import placeholder.organisation.unicms.service.StudentService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScheduleController.class)
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LessonService lessonService;

    @MockitoBean
    private StudentService studentService;

    @MockitoBean
    private LecturerService lecturerService;

    @Test
    void getStudentSchedule_shouldReturnStudentScheduleView_whenWeekStartProvided() throws Exception {
        LocalDate weekStart = LocalDate.of(2025, 3, 10);
        LocalDate weekEnd = weekStart.plusDays(6);

        when(studentService.findByEmail("bob@student.uni.com")).thenReturn(getStudent());
        when(lessonService.findLessonsInRange(weekStart, weekEnd, 1L)).thenReturn(List.of());

        mockMvc.perform(get("/my-schedule/student")
                        .param("weekStart", weekStart.toString())
                        .with(studentAuth()))
                .andExpect(status().isOk())
                .andExpect(view().name("student-schedule"))
                .andExpect(model().attribute("weekStart", weekStart))
                .andExpect(model().attribute("weekEnd", weekEnd))
                .andExpect(model().attribute("prevWeek", weekStart.minusWeeks(1)))
                .andExpect(model().attribute("nextWeek", weekStart.plusWeeks(1)))
                .andExpect(model().attributeExists("lessonsByDay"));
    }

    @Test
    void getStudentSchedule_shouldDefaultToCurrentWeek_whenWeekStartIsNull() throws Exception {
        LocalDate expectedWeekStart = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate expectedWeekEnd = expectedWeekStart.plusDays(6);

        when(studentService.findByEmail("bob@student.uni.com")).thenReturn(getStudent());
        when(lessonService.findLessonsInRange(expectedWeekStart, expectedWeekEnd, 1L)).thenReturn(List.of());

        mockMvc.perform(get("/my-schedule/student")
                        .with(studentAuth()))
                .andExpect(status().isOk())
                .andExpect(view().name("student-schedule"))
                .andExpect(model().attribute("weekStart", expectedWeekStart))
                .andExpect(model().attribute("weekEnd", expectedWeekEnd))
                .andExpect(model().attributeExists("lessonsByDay"));
    }

    @Test
    void getLecturerSchedule_shouldReturnLecturerScheduleView_whenWeekStartProvided() throws Exception {
        LocalDate weekStart = LocalDate.of(2025, 3, 10);
        LocalDate weekEnd = weekStart.plusDays(6);

        when(lecturerService.findByEmail("john@lecturer.uni.com")).thenReturn(getLecturer());
        when(lessonService.findLessonsInRange(weekStart, weekEnd, 2L)).thenReturn(List.of());

        mockMvc.perform(get("/my-schedule/lecturer")
                        .param("weekStart", weekStart.toString())
                        .with(lecturerAuth()))
                .andExpect(status().isOk())
                .andExpect(view().name("lecturer-schedule"))
                .andExpect(model().attribute("weekStart", weekStart))
                .andExpect(model().attribute("weekEnd", weekEnd))
                .andExpect(model().attribute("prevWeek", weekStart.minusWeeks(1)))
                .andExpect(model().attribute("nextWeek", weekStart.plusWeeks(1)))
                .andExpect(model().attributeExists("lessonsByDay"));
    }

    @Test
    void getLecturerSchedule_shouldDefaultToCurrentWeek_whenWeekStartIsNull() throws Exception {
        LocalDate expectedWeekStart = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate expectedWeekEnd = expectedWeekStart.plusDays(6);

        when(lecturerService.findByEmail("john@lecturer.uni.com")).thenReturn(getLecturer());
        when(lessonService.findLessonsInRange(expectedWeekStart, expectedWeekEnd, 2L)).thenReturn(List.of());

        mockMvc.perform(get("/my-schedule/lecturer")
                        .with(lecturerAuth()))
                .andExpect(status().isOk())
                .andExpect(view().name("lecturer-schedule"))
                .andExpect(model().attribute("weekStart", expectedWeekStart))
                .andExpect(model().attribute("weekEnd", expectedWeekEnd))
                .andExpect(model().attributeExists("lessonsByDay"));
    }

    private RequestPostProcessor studentAuth() {
        UserDetails userDetails = User.withUsername("bob@student.uni.com")
                .password("pass").roles("STUDENT").build();
        return authentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
    }

    private RequestPostProcessor lecturerAuth() {
        UserDetails userDetails = User.withUsername("john@lecturer.uni.com")
                .password("pass").roles("LECTURER").build();
        return authentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
    }

    private Student getStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setEmail("bob@student.uni.com");
        return student;
    }

    private Lecturer getLecturer() {
        Lecturer lecturer = new Lecturer();
        lecturer.setId(2L);
        lecturer.setEmail("john@lecturer.uni.com");
        return lecturer;
    }
}
