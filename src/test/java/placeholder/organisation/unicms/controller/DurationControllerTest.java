package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.service.DurationService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DurationController.class)
class DurationControllerTest {

    @MockitoBean
    DurationService durationService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getDurations_ShouldReturnViewAndModelAttributes_WhenPageableParametersProvided() throws Exception {
        List<Duration> durationList = List.of(new Duration(), new Duration());
        Page<Duration> durationPage = new PageImpl<>(durationList, PageRequest.of(0, 10), durationList.size());

        when(durationService.findAll(any(Pageable.class))).thenReturn(durationPage);

        mockMvc.perform(get("/durations")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(view().name("durations"))
                .andExpect(model().attribute("durations", durationList))
                .andExpect(model().attribute("page", durationPage))
                .andExpect(model().attribute("url", "durations"))
                .andExpect(model().attributeExists("durations", "page", "url"));

        verify(durationService).findAll(any(Pageable.class));
    }

    @Test
    void getDurations_ShouldThrowException(){

    }
}