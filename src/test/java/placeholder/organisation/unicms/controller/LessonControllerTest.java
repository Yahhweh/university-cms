package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Lesson;
import placeholder.organisation.unicms.service.LessonService;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LessonController.class)
class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LessonService lessonService;

    @MockitoBean
    private FieldExtractor fieldExtractor;

    @Test
    void getLessons_ShouldReturnTableViewWithAttributes() throws Exception {
        List<String> fields = List.of("id", "duration", "subject", "group", "lecturer", "room", "date");
        List<Lesson> lessons = List.of(new Lesson(), new Lesson());

        when(fieldExtractor.getFieldNames(Lesson.class)).thenReturn(fields);
        when(lessonService.findAllLessons()).thenReturn(lessons);

        mockMvc.perform(get("/lessons"))
                .andExpect(status().isOk())
                .andExpect(view().name("table"))
                .andExpect(model().attribute("fields", fields))
                .andExpect(model().attribute("objects", lessons))
                .andExpect(model().attributeExists("fields", "objects"));

        verify(fieldExtractor).getFieldNames(Lesson.class);
        verify(lessonService).findAllLessons();
    }
}