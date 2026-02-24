package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.RoomType;
import placeholder.organisation.unicms.service.RoomTypeService;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomTypeController.class)
class RoomTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoomTypeService roomTypeService;

    @Test
    void getRoomTypes_ShouldReturnTableViewWithAttributes() throws Exception {
        List<RoomType> roomTypes = List.of(new RoomType(), new RoomType());

        when(roomTypeService.findAllRoomTypes()).thenReturn(roomTypes);

        mockMvc.perform(get("/room-types"))
                .andExpect(status().isOk())
                .andExpect(view().name("roomTypes"))
                .andExpect(model().attribute("roomTypes", roomTypes))
                .andExpect(model().attributeExists( "roomTypes"));

        verify(roomTypeService).findAllRoomTypes();
    }
}