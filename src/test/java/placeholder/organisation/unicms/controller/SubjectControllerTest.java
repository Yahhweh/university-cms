package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Subject;
import placeholder.organisation.unicms.service.SubjectService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubjectController.class)
class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SubjectService subjectService;

    @Test
    void getRoomTypes_ShouldReturnTableViewWithAttributes_WhenAllDataIsGiven() throws Exception {
        List<Subject> subjects = List.of(new Subject(), new Subject());
        Page<Subject> subjectPage = new PageImpl<>(subjects, PageRequest.of(0, 10), subjects.size());

        when(subjectService.findAll(any(Pageable.class)))
                .thenReturn(subjectPage);

        mockMvc.perform(get("/subjects")
                        .param("sort", "asc")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("subjects"))
                .andExpect(model().attribute("subjects", subjects))
                .andExpect(model().attribute("url", "subjects"))
                .andExpect(model().attribute("page", subjectPage))
                .andExpect(model().attributeExists("subjects", "url", "page"));

        verify(subjectService).findAll(any(Pageable.class));
    }

}