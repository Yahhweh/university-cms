package placeholder.organisation.unicms.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.service.DurationService;

import java.util.List;

@Controller
public class DurationController {

    private final DurationService service;

    public DurationController(DurationService service) {
        this.service = service;
    }

    @GetMapping(value = "/durations")
    public String getDuration(Model model,
                              @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Duration> page = service.findAll(pageable);

        model.addAttribute("durations", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("url", "durations");

        return "durations";
    }
}