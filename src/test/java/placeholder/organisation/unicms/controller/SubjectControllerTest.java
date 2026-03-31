package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Subject;
import placeholder.organisation.unicms.service.SubjectService;
import placeholder.organisation.unicms.service.dto.request.filter.SubjectFilter;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubjectController.class)
@WithMockUser(username = "user", roles = {"ADMIN"})
class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SubjectService subjectService;

    @Test
    void getSubjects_shouldReturnSubjectsView_withPagedData() throws Exception {
        List<Subject> subjects = List.of(getSubject());
        Pageable pageable = PageRequest.of(0, 9, Sort.by("id").ascending());
        Page<Subject> subjectPage = new PageImpl<>(subjects, pageable, subjects.size());

        when(subjectService.findAll(any(Pageable.class), any(SubjectFilter.class))).thenReturn(subjectPage);

        mockMvc.perform(get("/subjects")
                .param("sort", "id,asc")
                .param("page", "0")
                .param("size", "9"))
            .andExpect(status().isOk())
            .andExpect(view().name("subjects"))
            .andExpect(model().attribute("subjects", subjectPage.getContent()))
            .andExpect(model().attribute("url", "admin/subjects"))
            .andExpect(model().attribute("page", subjectPage));
    }

    @Test
    void getSubjects_shouldFilterByName_whenNameParamProvided() throws Exception {
        List<Subject> subjects = List.of(getSubject());
        Pageable pageable = PageRequest.of(0, 9, Sort.by("id").ascending());
        Page<Subject> subjectPage = new PageImpl<>(subjects, pageable, subjects.size());

        when(subjectService.findAll(any(Pageable.class), any(SubjectFilter.class))).thenReturn(subjectPage);

        mockMvc.perform(get("/subjects")
                .param("name", "Physics"))
            .andExpect(status().isOk())
            .andExpect(view().name("subjects"))
            .andExpect(model().attribute("subjects", subjectPage.getContent()));

        verify(subjectService).findAll(any(Pageable.class), any(SubjectFilter.class));
    }

    @Test
    void showAddSubjectForm_shouldReturnAddSubjectView() throws Exception {
        mockMvc.perform(get("/add-subject"))
            .andExpect(status().isOk())
            .andExpect(view().name("add-subject"));
    }

    @Test
    void addSubject_shouldRedirectWithSuccess_whenSubjectCreated() throws Exception {
        mockMvc.perform(post("/add-subject")
                .param("name", "Physics")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("add-subject"))
            .andExpect(flash().attribute("successMessage", "Subject has been successfully added"));

        verify(subjectService).createSubject(any(placeholder.organisation.unicms.service.dto.request.SubjectRequestDTO.class));
    }

    @Test
    void deleteSubject_shouldRedirectToSubjects_whenSubjectDeleted() throws Exception {
        mockMvc.perform(post("/delete-subject")
                .param("subjectId", "1")
                .param("name", "")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("subjects*"))
            .andExpect(flash().attribute("successMessage", "Subject has been successfully deleted"));

        verify(subjectService).removeStudySubject(1L);
    }

    private Subject getSubject() {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Physics");
        return subject;
    }
}