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
import placeholder.organisation.unicms.entity.Room;
import placeholder.organisation.unicms.entity.RoomType;
import placeholder.organisation.unicms.service.RoomTypeService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
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
        List<RoomType> rooms = List.of(new RoomType(), new RoomType());
        Page<RoomType> roomPage = new PageImpl<>(rooms, PageRequest.of(0, 10), rooms.size());

        when(roomTypeService.findAll(any(Pageable.class)))
                .thenReturn(roomPage);

        mockMvc.perform(get("/room-types")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "asc"))
                .andExpect(status().isOk())
                .andExpect(view().name("roomTypes"))
                .andExpect(model().attribute("rooms", rooms))
                .andExpect(model().attribute("page", roomPage))
                .andExpect(model().attribute("url", "room-types"))
                .andExpect(model().attributeExists("rooms", "url", "page", "nextDir"));

        verify(roomTypeService).findAll(any(Pageable.class));
    }
}