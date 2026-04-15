package placeholder.organisation.unicms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import placeholder.organisation.unicms.entity.Room;
import placeholder.organisation.unicms.service.RoomService;
import placeholder.organisation.unicms.service.RoomTypeService;
import placeholder.organisation.unicms.service.dto.request.RoomRequestDTO;
import placeholder.organisation.unicms.service.dto.request.filter.RoomFilter;

@Controller
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
@RequestMapping("/rooms")
public class RoomController {

    private static final String ADD_ROOM_MESSAGE = "Room has been successfully created";
    private static final String VALIDATION_ADD_ROOM_MESSAGE = "Some of your forms are not valid";
    private static final String REMOVE_ROOM_MESSAGE = "Room has been successfully deleted";

    private final RoomService roomService;
    private final RoomTypeService roomTypeService;

    public RoomController(RoomService roomService, RoomTypeService roomTypeService) {
        this.roomService = roomService;
        this.roomTypeService = roomTypeService;
    }

    @GetMapping
    public String getRooms(Model model,
                           @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                           @ModelAttribute("filters") RoomFilter filter) {

        Page<Room> page = roomService.findAll(filter, pageable);
        model.addAttribute("rooms", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("roomTypes", roomTypeService.findAllRoomTypes());
        model.addAttribute("url", "rooms");
        return "rooms";
    }


    @GetMapping("/create-room")
    public String getCreateRoom(Model model) {
        model.addAttribute("roomTypes", roomTypeService.findAllRoomTypes());
        return "create-room";
    }

    @PostMapping("/create-room")
    public String createRoom(RedirectAttributes redirectAttributes, @ModelAttribute RoomRequestDTO roomRequestDTO,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", VALIDATION_ADD_ROOM_MESSAGE);
            return "redirect:create-room";
        }
        roomService.createRoom(roomRequestDTO);
        redirectAttributes.addFlashAttribute("successMessage", ADD_ROOM_MESSAGE);
        return "redirect:create-room";
    }

    @PostMapping("/delete-room")
    public String deleteRooms(RedirectAttributes redirectAttributes, @ModelAttribute("filters") RoomFilter roomFilter,
                              @RequestParam Long roomId, @PageableDefault(direction = Sort.Direction.ASC, sort = "id") Pageable pageable) {

        roomService.removeClassRoom(roomId);
        RedirectAttributesHelper.addPageAndFilterAttributes(REMOVE_ROOM_MESSAGE, pageable, redirectAttributes, roomFilter);
        return "redirect:rooms";
    }
}
