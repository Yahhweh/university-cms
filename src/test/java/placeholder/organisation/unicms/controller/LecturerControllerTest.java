package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.service.GroupService;
import placeholder.organisation.unicms.service.LecturerService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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

        when(lecturerService.findAllLecturers()).thenReturn(lecturers);

        mockMvc.perform(get("/lecturers"))
                .andExpect(status().isOk())
                .andExpect(view().name("lecturers"))
                .andExpect(model().attribute("lecturers", lecturers))
                .andExpect(model().attributeExists( "lecturers"));

        verify(lecturerService).findAllLecturers();
    }

}