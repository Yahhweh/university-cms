package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Degree;
import placeholder.organisation.unicms.entity.Lesson;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.service.GroupService;
import placeholder.organisation.unicms.service.LessonService;
import placeholder.organisation.unicms.service.StudentService;
import placeholder.organisation.unicms.service.util.ScheduleUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@WithMockUser(username = "user", roles = "ADMIN")
@Import({StudentControllerTest.MethodSecurityConfig.class, ScheduleUtil.class})
class StudentControllerTest {

    @TestConfiguration
    @EnableMethodSecurity
    static class MethodSecurityConfig {}

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    StudentService studentService;
    @MockitoBean
    GroupService groupService;
    @MockitoBean
    LessonService lessonService;

    @Test
    void getLecturers_ShouldReturnViewName_whenEverythingIsCorrect() throws Exception {
        List<Student> students = List.of(getStudent());

        Pageable pageable = PageRequest.of(0, 9, Sort.by("id").ascending());
        Page<Student> studentPage = new PageImpl<>(students, pageable, students.size());

        when(studentService.findAll(pageable)).thenReturn(studentPage);

        mockMvc.perform(get("/students")
                .param("sort", "id,asc")
                .param("page", "0")
                .param("size", "9"))
            .andExpect(status().isOk())
            .andExpect(view().name("students"))
            .andExpect(model().attribute("students", studentPage.getContent()))
            .andExpect(model().attribute("page", studentPage))
            .andExpect(model().attribute("url", "students"));
    }

    @Test
    @WithMockUser(username = "student@test.com", roles = "STUDENT")
    void getMySchedule_ShouldReturnScheduleView_whenWeekRange() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate weekBegin = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEnd = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));

        Student student = getStudent();
        when(studentService.findByEmail("student@test.com")).thenReturn(student);
        when(lessonService.findLessonsInRange(eq(weekBegin), eq(weekEnd), eq(student))).thenReturn(List.of());

        mockMvc.perform(get("/students/my-schedule")
                .param("startDate", weekBegin.toString())
                .param("endDate", weekEnd.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("student-schedule"));
    }

    @Test
    @WithMockUser(username = "student@test.com", roles = "STUDENT")
    void getMySchedule_ShouldReturnScheduleView_whenMonthRange() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate monthBegin = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate monthEnd = today.with(TemporalAdjusters.lastDayOfMonth());

        Student student = getStudent();
        when(studentService.findByEmail("student@test.com")).thenReturn(student);
        when(lessonService.findLessonsInRange(eq(monthBegin), eq(monthEnd), eq(student))).thenReturn(List.of());

        mockMvc.perform(get("/students/my-schedule")
                .param("startDate", monthBegin.toString())
                .param("endDate", monthEnd.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("student-schedule"));
    }

    @Test
    void getMySchedule_ShouldReturnForbidden_whenRoleIsAdmin() throws Exception {
        mockMvc.perform(get("/students/my-schedule"))
            .andExpect(status().isForbidden());
    }

    private Student getStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setName("Bob");
        student.setSureName("SureName");
        student.setDegree(Degree.Bachelor);
        return student;
    }
}