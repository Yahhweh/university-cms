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
    @MockitoBean
    FieldExtractor fieldExtractor;

    @Test
    void getLecturers_ShouldReturnViewName_whenEverythingIsCorrect() throws Exception {
        List<String> fields = new java.util.ArrayList<>(List.of("salary", "subjects"));
        fields.addAll(getPersonFields());
        List<Lecturer> lecturers = List.of(new Lecturer(), new Lecturer());

        when(fieldExtractor.getFieldNames(Lecturer.class)).thenReturn(fields);
        when(lecturerService.findAllLecturers()).thenReturn(lecturers);

        mockMvc.perform(get("/lecturers"))
                .andExpect(status().isOk())
                .andExpect(view().name("table"))
                .andExpect(model().attribute("fields", fields))
                .andExpect(model().attribute("objects", lecturers))
                .andExpect(model().attributeExists("fields", "objects"));

        verify(fieldExtractor).getFieldNames(Lecturer.class);
        verify(lecturerService).findAllLecturers();
    }


    List<String> getPersonFields(){
        return List.of("id", "password", "name", "sureName", "gender", "email", "address", "dateOfBirth");
    }
}