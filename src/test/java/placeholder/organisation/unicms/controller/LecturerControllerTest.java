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
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.service.GroupService;
import placeholder.organisation.unicms.service.LecturerService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
        List<Lecturer> lecturerList = List.of(new Lecturer(), new Lecturer());
        Page<Lecturer> lecturerPage = new PageImpl<>(lecturerList, PageRequest.of(0, 10), lecturerList.size());

        when(lecturerService.findAll(any(Pageable.class)))
                .thenReturn(lecturerPage);

        mockMvc.perform(get("/lecturers")
                        .param("size", "10")
                        .param("sort", "asc")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name("lecturers"))
                .andExpect(model().attribute("lecturers", lecturerList))
                .andExpect(model().attribute("page", lecturerPage))
                .andExpect(model().attribute("url", "lecturers"))
                .andExpect(model().attributeExists("lecturers", "page", "url"));

        verify(lecturerService).findAll(any(Pageable.class));
    }
}