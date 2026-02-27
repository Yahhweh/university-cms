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
import placeholder.organisation.unicms.service.RoomService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.*;

@WebMvcTest(RoomController.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoomService roomService;

    @Test
    void getRooms_ShouldReturnViewName_whenEverythingIsCorrect() throws Exception {
        List<Room> rooms = List.of(new Room(), new Room());
        Page<Room> roomPage = new PageImpl<>(rooms, PageRequest.of(0, 10), rooms.size());

        when(roomService.findAll(any(Pageable.class)))
                .thenReturn(roomPage);

        mockMvc.perform(get("/rooms")
                        .param("sort", "asc")
                        .param("size", "10")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name("rooms"))
                .andExpect(model().attribute("rooms", rooms))
                .andExpect(model().attribute("page", roomPage))
                .andExpect(model().attribute("url", "rooms"))
                .andExpect(model().attributeExists("rooms", "url", "page"));

        verify(roomService).findAll(any(Pageable.class));
    }
}