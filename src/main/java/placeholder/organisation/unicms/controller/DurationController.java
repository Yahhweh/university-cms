package placeholder.organisation.unicms.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.service.DurationService;
import placeholder.organisation.unicms.service.FilterAndSorterOfEntities;

import java.util.List;

@Controller
public class DurationController {

    DurationService service;

    public DurationController(DurationService service) {
        this.service = service;
    }

    @RequestMapping(path = "/durations", method = RequestMethod.GET)
    public String getDuration(Model model,
                              @RequestParam(defaultValue = "id") String sortField,
                              @RequestParam(defaultValue = "asc") String sortDirection){
        Page<Duration> page = service.getFilteredAndSortedDuration(sortField, sortDirection);

        List<Duration> durations = page.getContent();

        String nextDir = sortDirection.equals("asc") ? "desc" : (sortDirection.equals("desc") ? "none" : "asc");

        model.addAttribute("durations", durations);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("nextDir", nextDir);

        return "durations";
    }
}
