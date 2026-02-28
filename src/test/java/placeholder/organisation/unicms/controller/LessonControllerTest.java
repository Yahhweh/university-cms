package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Lesson;
import placeholder.organisation.unicms.service.LessonService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
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

    @Test
    void getLessons_ShouldReturnTableViewWithAttributes_WhenEverythingIsCorrect() throws Exception {
        List<Lesson> lessons = List.of(new Lesson(), new Lesson());
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
                .andExpect(model().attribute("url", "lessons"))
                .andExpect(model().attributeExists("lessons", "page", "url"));
    }
}