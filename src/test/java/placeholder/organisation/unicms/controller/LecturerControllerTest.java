package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.service.GroupService;
import placeholder.organisation.unicms.service.LecturerService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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
        Page<Lecturer> lecturerPage = new PageImpl<>(lecturerList);

        String sortField = "salary";
        String sortDir = "asc";
        int pageNo = 1;

        when(lecturerService.getFilteredAndSortedLecturers(anyString(), anyString(), anyInt()))
                .thenReturn(lecturerPage);

        mockMvc.perform(get("/lecturers")
                        .param("sortField", sortField)
                        .param("sortDirection", sortDir)
                        .param("pageNo", String.valueOf(pageNo)))
                .andExpect(status().isOk())
                .andExpect(view().name("lecturers"))
                .andExpect(model().attribute("lecturers", lecturerList))
                .andExpect(model().attribute("sortField", sortField))
                .andExpect(model().attribute("sortDirection", sortDir))
                .andExpect(model().attribute("nextDir", "desc"))
                .andExpect(model().attributeExists("lecturers", "sortField", "sortDirection", "nextDir"));

        verify(lecturerService).getFilteredAndSortedLecturers(sortField, sortDir, pageNo);
    }

}