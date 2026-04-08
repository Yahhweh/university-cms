package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Room;
import placeholder.organisation.unicms.entity.RoomType;
import placeholder.organisation.unicms.service.RoomService;
import placeholder.organisation.unicms.service.RoomTypeService;
import placeholder.organisation.unicms.service.dto.request.filter.RoomFilter;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomController.class)
@WithMockUser(username = "user", roles = {"ADMIN"})
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoomService roomService;

    @MockitoBean
    private RoomTypeService roomTypeService;

    @Test
    void getRooms_shouldReturnRoomsView_withPagedData() throws Exception {
        List<Room> rooms = List.of(getClassRoom());
        Pageable pageable = PageRequest.of(0, 9, Sort.by("id").ascending());
        Page<Room> roomPage = new PageImpl<>(rooms, pageable, rooms.size());

        when(roomService.findAll(any(RoomFilter.class), any(Pageable.class))).thenReturn(roomPage);

        mockMvc.perform(get("/rooms")
                .param("sort", "id,asc")
                .param("size", "9")
                .param("page", "0"))
            .andExpect(status().isOk())
            .andExpect(view().name("rooms"))
            .andExpect(model().attribute("rooms", rooms))
            .andExpect(model().attribute("page", roomPage))
            .andExpect(model().attribute("url", "rooms"));
    }

    @Test
    void getAddRoom_shouldReturnCreateRoomView_withRoomTypes() throws Exception {
        when(roomTypeService.findAllRoomTypes()).thenReturn(List.of());

        mockMvc.perform(get("/rooms/create-room"))

            .andExpect(status().isOk())
            .andExpect(view().name("create-room"))
            .andExpect(model().attributeExists("roomTypes"));
    }

    @Test
    void createRoom_shouldRedirectWithSuccess_whenRoomCreated() throws Exception {
        mockMvc.perform(post("/rooms/create-room")
                .param("number", "B-201")
                .param("roomTypeId", "1")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("create-room"))
            .andExpect(flash().attribute("successMessage", "Room has been successfully created"));

        verify(roomService).createRoom(any());
    }

    @Test
    void deleteRoom_shouldRedirectToRooms_whenRoomDeleted() throws Exception {
        mockMvc.perform(post("/rooms/delete-room")
                .param("roomId", "1")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("rooms*"));

        verify(roomService).removeClassRoom(1L);
    }

    private Room getClassRoom() {
        return new Room(1L, "A-101", new RoomType(1L, "Hall", 100L));
    }
}