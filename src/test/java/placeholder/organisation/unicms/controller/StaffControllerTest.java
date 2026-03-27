package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.service.DurationService;
import placeholder.organisation.unicms.service.LessonService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(StaffController.class)
@WithMockUser(username = "user", roles = {"STAFF"})
class StaffControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LessonService lessonService;

    @MockitoBean
    private DurationService durationService;

    @Test
    void getStaffButtons_shouldReturnStaffView() throws Exception {
        mockMvc.perform(get("/staff"))
            .andExpect(status().isOk())
            .andExpect(view().name("staff"));
    }

}