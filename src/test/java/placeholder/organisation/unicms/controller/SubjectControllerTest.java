package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Subject;
import placeholder.organisation.unicms.service.SubjectService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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
    void getRoomTypes_ShouldReturnTableViewWithAttributes() throws Exception {
        List<Subject> subjects = List.of(new Subject(), new Subject());
        Page<Subject> subjectPage = new PageImpl<>(subjects);

        String sortField = "salary";
        String sortDir = "asc";
        int pageNo = 1;

        when(subjectService.getFilteredAndSortedSubject(anyString(), anyString(), anyInt()))
                .thenReturn(subjectPage);

        mockMvc.perform(get("/subjects")
                        .param("sortField", sortField)
                        .param("sortDirection", sortDir)
                        .param("pageNo", String.valueOf(pageNo)))
                .andExpect(status().isOk())
                .andExpect(view().name("subjects"))
                .andExpect(model().attribute("subjects", subjects))
                .andExpect(model().attribute("sortField", sortField))
                .andExpect(model().attribute("sortDirection", sortDir))
                .andExpect(model().attribute("nextDir", "desc"))
                .andExpect(model().attributeExists("subjects", "sortField", "sortDirection", "nextDir"));

        verify(subjectService).getFilteredAndSortedSubject(sortField, sortDir, pageNo);
    }
}