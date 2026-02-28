package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
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
        Pageable pageable = PageRequest.of(0, 9, Sort.by("id").ascending());
        Page<Subject> subjectPage = new PageImpl<>(subjects, pageable, subjects.size());

        when(subjectService.findAll(pageable))
                .thenReturn(subjectPage);

        mockMvc.perform(get("/subjects")
                        .param("sort", "id,asc")
                        .param("page", "0")
                        .param("size", "9"))
                .andExpect(status().isOk())
                .andExpect(view().name("subjects"))
                .andExpect(model().attribute("subjects", subjectPage.getContent()))
                .andExpect(model().attribute("url", "subjects"))
                .andExpect(model().attribute("page", subjectPage))
                .andExpect(model().attributeExists("subjects", "url", "page"));
    }

}