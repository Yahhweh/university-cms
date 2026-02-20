package placeholder.organisation.unicms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.service.DurationService;

import java.util.List;

@Controller
public class DurationController {

    DurationService durationService;
    FieldExtractor fieldExtractor;

    public DurationController(DurationService durationService, FieldExtractor fieldExtractor) {
        this.durationService = durationService;
        this.fieldExtractor = fieldExtractor;
    }

    @RequestMapping(path = "/durations", method = RequestMethod.GET)
    public String getDuration(Model model){
        List<String> fields = fieldExtractor.getFieldNames(Duration.class);
        List<Duration> durations = durationService.findAllDurations();

        model.addAttribute("fields", fields);
        model.addAttribute("objects", durations);

        return "table";
    }
}
