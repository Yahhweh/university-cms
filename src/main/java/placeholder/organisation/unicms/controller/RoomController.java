package placeholder.organisation.unicms.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import placeholder.organisation.unicms.entity.Room;
import placeholder.organisation.unicms.service.RoomService;

import java.util.List;

@Controller
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @RequestMapping(path = "/rooms", method = RequestMethod.GET)
    public String getRooms(Model model,
                           @RequestParam(defaultValue = "id") String sortField,
                           @RequestParam(defaultValue = "asc") String sortDirection,
                           @RequestParam(value = "pageNo", defaultValue = "1") int pageNo){

        Page<Room> page = roomService.getFilteredAndSortedRoom(sortField, sortDirection, pageNo);

        List<Room> rooms = page.getContent();

        String url = "rooms";

        String nextDir = sortDirection.equals("asc") ? "desc" : (sortDirection.equals("desc") ? "none" : "asc");

        model.addAttribute("rooms", rooms);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("nextDir", nextDir);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("url", url);

        return "rooms";
    }
}
