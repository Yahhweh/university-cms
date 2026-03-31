package placeholder.organisation.unicms.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.service.DurationService;
import placeholder.organisation.unicms.service.dto.request.DurationRequestDTO;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("admin/")
@Validated
public class DurationController {

    private final static String validationAddDurationMessage = "Some of your forms are not valid";
    private final static String successAddDurationMessage = "Duration has been successfully created";
    private final static String successRemoveDurationMessage = "Duration has been successfully deleted";

    private final DurationService durationService;

    public DurationController(DurationService service) {
        this.durationService = service;
    }

    @GetMapping(value = "/durations")
    public String getDuration(Model model,
                              @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<Duration> page = durationService.findAll(pageable);

        model.addAttribute("durations", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("url", "durations");

        return "durations";
    }

    @GetMapping("add-duration")
    public String getAddDuration() {
        return "add-duration";
    }

    @PostMapping("/add-duration")
    public String addDuration(RedirectAttributes redirectAttributes,
                              @ModelAttribute() DurationRequestDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", validationAddDurationMessage);
        }
        durationService.createDuration(dto);
        redirectAttributes.addFlashAttribute("successMessage", successAddDurationMessage);
        return "redirect:add-duration";
    }

    @PostMapping("/delete-duration")
    public String deleteDuration(RedirectAttributes redirectAttributes, @RequestParam Long durationId,
                                 @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        durationService.removeDuration(durationId);
        addRedirectAttributes(pageable, redirectAttributes);
        return "redirect:durations";
    }

    private void addRedirectAttributes(Pageable pageable, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("successMessage", successRemoveDurationMessage);
        redirectAttributes.addAttribute("page", pageable.getPageNumber());
        pageable.getSort().forEach(order ->
            redirectAttributes.addAttribute("sort",
                order.getProperty() + "," + order.getDirection().name().toLowerCase())
        );
    }
}