package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.entity.Lesson;
import placeholder.organisation.unicms.service.LessonService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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
    void getLessons_ShouldReturnTableViewWithAttributes() throws Exception {
        List<Lesson> lessonList = List.of(new Lesson(), new Lesson());
        Page<Lesson> lessonPage = new PageImpl<>(lessonList);

        String sortField = "salary";
        String sortDir = "asc";
        int pageNo = 1;

        when(lessonService.getFilteredAndSortedLesson(anyString(), anyString(), anyInt()))
                .thenReturn(lessonPage);

        mockMvc.perform(get("/lessons")
                        .param("sortField", sortField)
                        .param("sortDirection", sortDir)
                        .param("pageNo", String.valueOf(pageNo)))
                .andExpect(status().isOk())
                .andExpect(view().name("lessons"))
                .andExpect(model().attribute("lessons", lessonList))
                .andExpect(model().attribute("sortField", sortField))
                .andExpect(model().attribute("sortDirection", sortDir))
                .andExpect(model().attribute("nextDir", "desc"))
                .andExpect(model().attributeExists("lessons", "sortField", "sortDirection", "nextDir"));

        verify(lessonService).getFilteredAndSortedLesson(sortField, sortDir, pageNo);
    }
}