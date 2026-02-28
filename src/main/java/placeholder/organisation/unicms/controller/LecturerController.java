package placeholder.organisation.unicms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.service.LecturerService;

import java.util.List;

@Controller
public class LecturerController {

    private final LecturerService service;

    public LecturerController(LecturerService lecturerService) {
        this.service = lecturerService;
    }

    @GetMapping(value = "/lecturers")
    public String getLecturers(Model model,
                               @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Lecturer> page = service.findAll(pageable);

        model.addAttribute("lecturers", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("url", "lecturers");

        return "lecturers";
    }
}
