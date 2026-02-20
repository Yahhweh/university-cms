package placeholder.organisation.unicms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import placeholder.organisation.unicms.entity.Room;
import placeholder.organisation.unicms.service.RoomService;

import java.util.List;

@Controller
public class RoomController {

    RoomService roomService;
    FieldExtractor fieldExtractor;

    public RoomController(RoomService roomService, FieldExtractor fieldExtractor) {
        this.roomService = roomService;
        this.fieldExtractor = fieldExtractor;
    }

    @RequestMapping(path = "/rooms", method = RequestMethod.GET)
    public String getRooms(Model model){
        List<String> fields = fieldExtractor.getFieldNames(Room.class);
        List<Room> rooms = roomService.findAllRooms();

        model.addAttribute("fields", fields);
        model.addAttribute("objects", rooms);

        return "table";
    }
}
