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
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.entity.Lesson;
import placeholder.organisation.unicms.service.LecturerService;
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

@WebMvcTest(LecturerController.class)
@WithMockUser(username = "user", roles = "ADMIN")
@Import({LecturerControllerTest.MethodSecurityConfig.class, ScheduleUtil.class})
class LecturerControllerTest {

    @TestConfiguration
    @EnableMethodSecurity
    static class MethodSecurityConfig {}

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    LecturerService lecturerService;
    @MockitoBean
    LessonService lessonService;
    @MockitoBean
    StudentService studentService;

    @Test
    void getLecturers_ShouldReturnViewName_whenEverythingIsCorrect() throws Exception {
        List<Lecturer> lecturers = List.of(getLecturer());
        Pageable pageable = PageRequest.of(0, 9, Sort.by("id").ascending());
        Page<Lecturer> lecturerPage = new PageImpl<>(lecturers, pageable, lecturers.size());

        when(lecturerService.findAll(pageable)).thenReturn(lecturerPage);

        mockMvc.perform(get("/lecturers")
                .param("size", "9")
                .param("sort", "id,asc")
                .param("page", "0"))
            .andExpect(status().isOk())
            .andExpect(view().name("lecturers"))
            .andExpect(model().attribute("lecturers", lecturerPage.getContent()))
            .andExpect(model().attribute("page", lecturerPage))
            .andExpect(model().attribute("url", "lecturers"));
    }

    @Test
    @WithMockUser(username = "lecturer@test.com", roles = "LECTURER")
    void getSchedule_ShouldReturnScheduleView_whenWeekRange() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate weekBegin = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEnd = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));

        Lecturer lecturer = getLecturer();
        when(lecturerService.findByEmail("lecturer@test.com")).thenReturn(lecturer);
        when(lessonService.findLessonsInRange(eq(weekBegin), eq(weekEnd), eq(lecturer))).thenReturn(List.of());

        mockMvc.perform(get("/lecturers/my-schedule")
                .param("startDate", weekBegin.toString())
                .param("endDate", weekEnd.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("lecturer-schedule"));
    }

    @Test
    @WithMockUser(username = "lecturer@test.com", roles = "LECTURER")
    void getSchedule_ShouldReturnScheduleView_whenMonthRange() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate monthBegin = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate monthEnd = today.with(TemporalAdjusters.lastDayOfMonth());

        Lecturer lecturer = getLecturer();
        when(lecturerService.findByEmail("lecturer@test.com")).thenReturn(lecturer);
        when(lessonService.findLessonsInRange(eq(monthBegin), eq(monthEnd), eq(lecturer))).thenReturn(List.of());

        mockMvc.perform(get("/lecturers/my-schedule")
                .param("startDate", monthBegin.toString())
                .param("endDate", monthEnd.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("lecturer-schedule"));
    }

    @Test
    void getSchedule_ShouldReturnForbidden_whenRoleIsAdmin() throws Exception {
        mockMvc.perform(get("/lecturers/my-schedule"))
            .andExpect(status().isForbidden());
    }

    private Lecturer getLecturer() {
        Lecturer lecturer = new Lecturer();
        lecturer.setId(1L);
        lecturer.setName("John");
        lecturer.setSureName("Doe");
        lecturer.setSalary(40000);
        return lecturer;
    }
}