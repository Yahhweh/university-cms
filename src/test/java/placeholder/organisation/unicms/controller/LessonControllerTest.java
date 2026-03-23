package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.service.LessonService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LessonController.class)
@WithMockUser(username = "user", roles = {"ADMIN"})

class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LessonService lessonService;

    @Test
    void getLessons_ShouldReturnTableViewWithAttributes_WhenEverythingIsCorrect() throws Exception {
        List<Lesson> lessons = List.of(getLesson());
        Pageable pageable = PageRequest.of(0, 9, Sort.by("id").ascending());
        Page<Lesson> lessonPage = new PageImpl<>(lessons,pageable, lessons.size());

        when(lessonService.findAll(pageable))
                .thenReturn(lessonPage);

        mockMvc.perform(get("/lessons")
                        .param("sort", "id,asc")
                        .param("size", "9")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name("lessons"))
                .andExpect(model().attribute("lessons", lessons))
                .andExpect(model().attribute("page", lessonPage))
                .andExpect(model().attribute("url", "lessons"));
    }

    private Lesson getLesson() {
        return new Lesson(1L, getDuration(), new Subject(1L, "Math"), getGroup(), getLecturer(), getClassRoom(), LocalDate.now());
    }


    private Lecturer getLecturer() {
        Lecturer lecturer = new Lecturer();
        lecturer.setId(1L);
        lecturer.setName("John");
        lecturer.setSureName("Pork");
        lecturer.setSalary(40000);
        return lecturer;
    }

    private Group getGroup() {
        return new Group(1L, "A-122");
    }

    private Duration getDuration() {
        return new Duration(1L, LocalTime.of(8, 30), LocalTime.of(10, 00));
    }

    private Room getClassRoom() {
        return new Room(1L, "A-101", new RoomType(1L, "Hall", 100L));
    }
}