package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import placeholder.organisation.unicms.entity.Degree;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.service.GroupService;
import placeholder.organisation.unicms.service.LessonService;
import placeholder.organisation.unicms.service.StudentService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(StudentController.class)
@WithMockUser(username = "user", roles = "ADMIN")
class StudentControllerTest {

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
        when(lessonService.findLessonsInRange(eq(weekBegin), eq(weekEnd), eq(student.getId()))).thenReturn(List.of());

        mockMvc.perform(get("/students/my-schedule")
                .param("begin", weekBegin.toString())
                .param("end", weekEnd.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("student-schedule"))
            .andExpect(model().attribute("begin", weekBegin))
            .andExpect(model().attribute("end", weekEnd))
            .andExpect(model().attribute("lessonsByDay", Map.of()));
    }

    @Test
    @WithMockUser(username = "student@test.com", roles = "STUDENT")
    void getMySchedule_ShouldReturnScheduleView_whenMonthRange() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate monthBegin = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate monthEnd = today.with(TemporalAdjusters.lastDayOfMonth());

        Student student = getStudent();
        when(studentService.findByEmail("student@test.com")).thenReturn(student);
        when(lessonService.findLessonsInRange(eq(monthBegin), eq(monthEnd), eq(student.getId()))).thenReturn(List.of());

        mockMvc.perform(get("/students/my-schedule")
                .param("begin", monthBegin.toString())
                .param("end", monthEnd.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("student-schedule"))
            .andExpect(model().attribute("begin", monthBegin))
            .andExpect(model().attribute("end", monthEnd))
            .andExpect(model().attribute("lessonsByDay", Map.of()));
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
