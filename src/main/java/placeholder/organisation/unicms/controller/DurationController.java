package placeholder.organisation.unicms.controller;

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

    private static final String VALIDATION_CREATE_DURATION_MESSAGE = "Some of your forms are not valid";
    private static final String CREATE_DURATION_MESSAGE = "Duration has been successfully created";
    private static final String REMOVE_DURATION_MESSAGE = "Duration has been successfully deleted";

    private final DurationService durationService;

    public DurationController(DurationService service) {
        this.durationService = service;
    }

    @GetMapping( "/durations")
    public String getDuration(Model model,
                              @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<Duration> page = durationService.findAll(pageable);

        model.addAttribute("durations", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("url", "durations");

        return "durations";
    }

    @GetMapping("create-duration")
    public String getCreateDuration() {
        return "create-duration";
    }

    @PostMapping("/create-duration")
    public String createDuration(RedirectAttributes redirectAttributes,
                              @ModelAttribute() DurationRequestDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", VALIDATION_CREATE_DURATION_MESSAGE);
        }
        durationService.createDuration(dto);
        redirectAttributes.addFlashAttribute("successMessage", CREATE_DURATION_MESSAGE);
        return "redirect:create-duration";
    }

    @PostMapping("/delete-duration")
    public String deleteDuration(RedirectAttributes redirectAttributes, @RequestParam Long durationId,
                                 @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        durationService.removeDuration(durationId);
        RedirectAttributesHelper.addPageAndFilterAttributes(REMOVE_DURATION_MESSAGE, pageable, redirectAttributes);
        return "redirect:durations";
    }
}