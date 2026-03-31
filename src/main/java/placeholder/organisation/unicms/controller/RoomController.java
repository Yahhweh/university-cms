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
import placeholder.organisation.unicms.service.EntityValidationException;
import placeholder.organisation.unicms.service.RoomService;
import placeholder.organisation.unicms.service.RoomTypeService;
import placeholder.organisation.unicms.service.dto.request.RoomRequestDTO;
import placeholder.organisation.unicms.service.dto.request.filter.RoomFilter;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class RoomController {

    private final static String successAddRoomMessage = "Room has been successfully created";
    private final static String validationAddRoomMessage = "Some of your forms are not valid";
    private final static String successRemoveRoomMessage = "Room has been successfully deleted";

    private final RoomService roomService;
    private final RoomTypeService roomTypeService;

    public RoomController(RoomService roomService, RoomTypeService roomTypeService) {
        this.roomService = roomService;
        this.roomTypeService = roomTypeService;
    }

    @GetMapping(value = "/rooms")
    public String getRooms(Model model,
                           @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                           @ModelAttribute("filters") RoomFilter requestDTO) {

        Page<Room> page = roomService.findAll(requestDTO, pageable);
        model.addAttribute("rooms", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("roomTypes", roomTypeService.findAllRoomTypes());
        model.addAttribute("url", "admin/rooms");
        return "rooms";
    }


    @GetMapping(value = "/add-room")
    public String getAddRoom(Model model) {
        model.addAttribute("roomTypes", roomTypeService.findAllRoomTypes());
        return "add-room";
    }

    @PostMapping(value = "/add-room")
    public String addRoom(RedirectAttributes redirectAttributes, @ModelAttribute RoomRequestDTO roomRequestDTO,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", validationAddRoomMessage);
            return "redirect:add-room";
        }
        roomService.createRoom(roomRequestDTO);
        redirectAttributes.addFlashAttribute("successMessage", successAddRoomMessage);
        return "redirect:add-room";
    }

    @PostMapping(value = "/delete-room")
    public String deleteRooms(RedirectAttributes redirectAttributes, @ModelAttribute("filters") RoomFilter roomFilter,
                              @RequestParam Long roomId, @PageableDefault(direction = Sort.Direction.ASC, sort = "id") Pageable pageable) {

        roomService.removeClassRoom(roomId);
        addRedirectAttributes(pageable, roomFilter, redirectAttributes);
        return "redirect:rooms";
    }

    private void addRedirectAttributes(Pageable pageable, RoomFilter requestDTO, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("successMessage", successRemoveRoomMessage);
        redirectAttributes.addAttribute("page", pageable.getPageNumber());
        pageable.getSort().forEach(order ->
            redirectAttributes.addAttribute("sort",
                order.getProperty() + "," + order.getDirection().name().toLowerCase())
        );
        redirectAttributes.addAttribute("roomNumber", requestDTO.getNumber() != null? requestDTO.getNumber() : "");
        redirectAttributes.addAttribute("roomType", requestDTO.getRoomType() != null ? requestDTO.getRoomType().toLowerCase() : "");
    }
}
