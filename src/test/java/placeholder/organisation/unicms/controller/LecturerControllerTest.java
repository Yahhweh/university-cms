package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.service.LecturerService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(LecturerController.class)
class LecturerControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    LecturerService lecturerService;

    @Test
    void getLecturers_ShouldReturnViewName_whenEverythingIsCorrect() throws Exception {
        List<Lecturer> lecturers = List.of(new Lecturer(), new Lecturer());
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
                .andExpect(model().attribute("url", "lecturers"))
                .andExpect(model().attributeExists("lecturers", "page", "url"));

        verify(lecturerService).findAll(any(Pageable.class));
    }
}