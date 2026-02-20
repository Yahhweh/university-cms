package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Room;
import placeholder.organisation.unicms.service.RoomService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomController.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoomService roomService;

    @MockitoBean
    private FieldExtractor fieldExtractor;


    @Test
    void getRooms_ShouldReturnViewName_whenEverythingIsCorrect() throws Exception {
        List<String> fields = List.of("id", "room", "roomType");
        List<Room> rooms = List.of(new Room(), new Room());

        when(fieldExtractor.getFieldNames(Room.class)).thenReturn(fields);
        when(roomService.findAllRooms()).thenReturn(rooms);

        mockMvc.perform(get("/rooms"))
                .andExpect(status().isOk())
                .andExpect(view().name("table"))
                .andExpect(model().attribute("fields", fields))
                .andExpect(model().attribute("objects", rooms))
                .andExpect(model().attributeExists("fields", "objects"));

        verify(fieldExtractor).getFieldNames(Room.class);
        verify(roomService).findAllRooms();
    }
}