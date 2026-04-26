package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import placeholder.organisation.unicms.entity.Degree;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.service.GroupService;
import placeholder.organisation.unicms.service.LessonService;
import placeholder.organisation.unicms.service.StudentService;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
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
    void getMySchedule_ShouldReturnScheduleView_whenWeekPeriod() throws Exception {
        Student student = getStudent();
        when(studentService.findByEmail("student@test.com")).thenReturn(student);
        when(lessonService.findLessonsInRange(any(), any(), eq(student.getId()))).thenReturn(List.of());

        mockMvc.perform(get("/students/my-schedule").param("period", "week"))
            .andExpect(status().isOk())
            .andExpect(view().name("student-schedule"))
            .andExpect(model().attribute("period", "week"))
            .andExpect(model().attribute("lessonsByDay", Map.of()));
    }

    @Test
    @WithMockUser(username = "student@test.com", roles = "STUDENT")
    void getMySchedule_ShouldReturnScheduleView_whenMonthPeriod() throws Exception {
        Student student = getStudent();
        when(studentService.findByEmail("student@test.com")).thenReturn(student);
        when(lessonService.findLessonsInRange(any(), any(), eq(student.getId()))).thenReturn(List.of());

        mockMvc.perform(get("/students/my-schedule").param("period", "month"))
            .andExpect(status().isOk())
            .andExpect(view().name("student-schedule"))
            .andExpect(model().attribute("period", "month"))
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