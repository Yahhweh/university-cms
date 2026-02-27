package placeholder.organisation.unicms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import placeholder.organisation.unicms.entity.Lesson;
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
                           @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Room> page = roomService.findAll(pageable);

        model.addAttribute("rooms", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("url", "rooms");

        return "rooms";
    }
}
