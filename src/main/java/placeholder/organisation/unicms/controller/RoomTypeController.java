package placeholder.organisation.unicms.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import placeholder.organisation.unicms.entity.RoomType;
import placeholder.organisation.unicms.service.RoomTypeService;

import java.util.List;

@Controller
public class RoomTypeController {

    RoomTypeService roomTypeService;

    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @RequestMapping(path = "/room-types", method = RequestMethod.GET)
    public String getRoomTypes(Model model,
                               @RequestParam(defaultValue = "id") String sortField,
                               @RequestParam(defaultValue = "asc") String sortDirection,
                               @RequestParam(value = "pageNo", defaultValue = "1") int pageNo)
    {

        Page<RoomType> page = roomTypeService.getFilteredAndSortedRoomType(sortField, sortDirection, pageNo);

        List<RoomType> roomTypes = page.getContent();

        String nextDir = sortDirection.equals("asc") ? "desc" : (sortDirection.equals("desc") ? "none" : "asc");

        model.addAttribute("roomTypes", roomTypes);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("nextDir", nextDir);
        model.addAttribute("pageNo", pageNo);

        return "roomTypes";
    }
}
