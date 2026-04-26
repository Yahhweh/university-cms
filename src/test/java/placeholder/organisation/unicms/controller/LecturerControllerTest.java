package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.service.LecturerService;
import placeholder.organisation.unicms.service.LessonService;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(LecturerController.class)
@WithMockUser(username = "user", roles = "ADMIN")
class LecturerControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    LecturerService lecturerService;
    @MockitoBean
    LessonService lessonService;

    @Test
    void getLecturers_ShouldReturnViewName_whenEverythingIsCorrect() throws Exception {
        List<Lecturer> lecturers = List.of(getLecturer());
        Pageable pageable = PageRequest.of(0, 9, Sort.by("id").ascending());
        Page<Lecturer> lecturerPage = new PageImpl<>(lecturers, pageable, lecturers.size());

        when(lecturerService.findAll(pageable))
            .thenReturn(lecturerPage);

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
    void getMySchedule_ShouldReturnScheduleView_whenWeekPeriod() throws Exception {
        Lecturer lecturer = getLecturer();
        when(lecturerService.findByEmail("lecturer@test.com")).thenReturn(lecturer);
        when(lessonService.findLessonsInRange(any(), any(), eq(lecturer.getId()))).thenReturn(List.of());

        mockMvc.perform(get("/lecturers/my-schedule").param("period", "week"))
            .andExpect(status().isOk())
            .andExpect(view().name("lecturer-schedule"))
            .andExpect(model().attribute("period", "week"))
            .andExpect(model().attribute("lessonsByDay", Map.of()));
    }

    @Test
    @WithMockUser(username = "lecturer@test.com", roles = "LECTURER")
    void getMySchedule_ShouldReturnScheduleView_whenMonthPeriod() throws Exception {
        Lecturer lecturer = getLecturer();
        when(lecturerService.findByEmail("lecturer@test.com")).thenReturn(lecturer);
        when(lessonService.findLessonsInRange(any(), any(), eq(lecturer.getId()))).thenReturn(List.of());

        mockMvc.perform(get("/lecturers/my-schedule").param("period", "month"))
            .andExpect(status().isOk())
            .andExpect(view().name("lecturer-schedule"))
            .andExpect(model().attribute("period", "month"))
            .andExpect(model().attribute("lessonsByDay", Map.of()));
    }

    @Test
    void getMySchedule_ShouldReturnForbidden_whenRoleIsAdmin() throws Exception {
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