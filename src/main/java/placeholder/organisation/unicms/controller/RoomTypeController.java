package placeholder.organisation.unicms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import placeholder.organisation.unicms.entity.RoomType;
import placeholder.organisation.unicms.service.RoomTypeService;

import java.util.List;

@Controller
public class RoomTypeController {

    RoomTypeService roomTypeService;
    FieldExtractor fieldExtractor;

    public RoomTypeController(RoomTypeService roomTypeService, FieldExtractor fieldExtractor) {
        this.roomTypeService = roomTypeService;
        this.fieldExtractor = fieldExtractor;
    }

    @RequestMapping(path = "/room-types", method = RequestMethod.GET)
    public String getRoomTypes(Model model) {
        List<String> fields = fieldExtractor.getFieldNames(RoomType.class);
        List<RoomType> roomTypes = roomTypeService.findAllRoomTypes();

        model.addAttribute("fields", fields);
        model.addAttribute("objects", roomTypes);

        return "table";
    }
}
