package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.entity.Room;
import placeholder.organisation.unicms.service.DurationService;
import placeholder.organisation.unicms.service.RoomService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(DurationController.class)
class DurationControllerTest {
    @MockitoBean
    DurationService durationService;
    @MockitoBean
    FieldExtractor fieldExtractor;
    @Autowired
    MockMvc mockMvc;

    @Test
    void getDurations_ShouldReturnViewName_whenEverythingIsCorrect() throws Exception {
        List<String> fields = List.of("id", "start", "end");
        List<Duration> durations = List.of(new Duration(), new Duration());

        when(fieldExtractor.getFieldNames(Duration.class)).thenReturn(fields);
        when(durationService.findAllDurations()).thenReturn(durations);

        mockMvc.perform(get("/durations"))
                .andExpect(status().isOk())
                .andExpect(view().name("table"))
                .andExpect(model().attribute("fields", fields))
                .andExpect(model().attribute("objects", durations))
                .andExpect(model().attributeExists("fields", "objects"));

        verify(fieldExtractor).getFieldNames(Duration.class);
        verify(durationService).findAllDurations();
    }
}