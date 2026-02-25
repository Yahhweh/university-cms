package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Room;
import placeholder.organisation.unicms.service.RoomService;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomController.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoomService roomService;

    @Test
    void getRooms_ShouldReturnViewName_whenEverythingIsCorrect() throws Exception {
        List<Room> rooms = List.of(new Room(), new Room());
        Page<Room> roomPage = new PageImpl<>(rooms);

        String sortField = "salary";
        String sortDir = "asc";
        int pageNo = 1;

        when(roomService.getFilteredAndSortedRoom(anyString(), anyString(), anyInt()))
                .thenReturn(roomPage);

        mockMvc.perform(get("/rooms")
                        .param("sortField", sortField)
                        .param("sortDirection", sortDir)
                        .param("pageNo", String.valueOf(pageNo)))
                .andExpect(status().isOk())
                .andExpect(view().name("rooms"))
                .andExpect(model().attribute("rooms", rooms))
                .andExpect(model().attribute("sortField", sortField))
                .andExpect(model().attribute("sortDirection", sortDir))
                .andExpect(model().attribute("nextDir", "desc"))
                .andExpect(model().attributeExists("rooms", "sortField", "sortDirection", "nextDir"));

        verify(roomService).getFilteredAndSortedRoom(sortField, sortDir, pageNo);
    }
}