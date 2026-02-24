package placeholder.organisation.unicms.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.service.LecturerService;

import java.util.List;

@Controller
public class LecturerController {

    LecturerService service;

    public LecturerController(LecturerService lecturerService) {
        this.service = lecturerService;
    }

    @RequestMapping(value = "/lecturers", method = RequestMethod.GET)
    public String getLecturers(Model model,
                               @RequestParam(defaultValue = "id") String sortField,
                               @RequestParam(defaultValue = "asc") String sortDirection,
                               @RequestParam(value = "pageNo", defaultValue = "1") int pageNo){
        Page<Lecturer> page = service.getFilteredAndSortedLecturers(sortField, sortDirection, pageNo);

        List<Lecturer> lecturers = page.getContent();

        String nextDir = sortDirection.equals("asc") ? "desc" : (sortDirection.equals("desc") ? "none" : "asc");

        model.addAttribute("lecturers", lecturers);
        model.addAttribute("currentSortField", sortField);
        model.addAttribute("currentSortDir", sortDirection);
        model.addAttribute("nextDir", nextDir);

        return "lecturers";
    }
}
