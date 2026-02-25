package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.entity.Room;
import placeholder.organisation.unicms.service.DurationService;
import placeholder.organisation.unicms.service.RoomService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(DurationController.class)
class DurationControllerTest {
    @MockitoBean
    DurationService durationService;
    @Autowired
    MockMvc mockMvc;

    @Test
    void getDurations_ShouldReturnViewAndModelAttributes_WhenParametersProvided() throws Exception {
        List<Duration> durationList = List.of(new Duration(), new Duration());
        Page<Duration> durationPage = new PageImpl<>(durationList);

        String sortField = "id";
        String sortDir = "asc";
        int pageNo = 1;

        when(durationService.getFilteredAndSortedDuration(anyString(), anyString(), anyInt()))
                .thenReturn(durationPage);

        mockMvc.perform(get("/durations")
                        .param("sortField", sortField)
                        .param("sortDirection", sortDir)
                        .param("pageNo", String.valueOf(pageNo)))
                .andExpect(status().isOk())
                .andExpect(view().name("durations"))
                .andExpect(model().attribute("durations", durationList))
                .andExpect(model().attribute("sortField", sortField))
                .andExpect(model().attribute("sortDirection", sortDir))
                .andExpect(model().attribute("nextDir", "desc"))
                .andExpect(model().attributeExists("durations", "sortField", "sortDirection", "nextDir"));

        verify(durationService).getFilteredAndSortedDuration(sortField, sortDir, pageNo);
    }
}