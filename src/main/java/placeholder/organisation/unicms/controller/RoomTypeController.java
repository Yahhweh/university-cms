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
import placeholder.organisation.unicms.entity.Room;
import placeholder.organisation.unicms.entity.RoomType;
import placeholder.organisation.unicms.service.RoomTypeService;

import java.util.List;

@Controller
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @RequestMapping(path = "/room-types", method = RequestMethod.GET)
    public String getRoomTypes(Model model,
                               @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<RoomType> page = roomTypeService.findAll(pageable);

        model.addAttribute("roomTypes", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("url", "roomTypes");

        return "roomTypes";
    }
}
