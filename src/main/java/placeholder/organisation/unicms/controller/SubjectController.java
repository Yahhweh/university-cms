package placeholder.organisation.unicms.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import placeholder.organisation.unicms.entity.Subject;
import placeholder.organisation.unicms.service.SubjectService;

import java.util.List;

@Controller
public class SubjectController {

    SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @RequestMapping(path = "/subjects", method = RequestMethod.GET)
    public String getRoomTypes(Model model,
                               @RequestParam(defaultValue = "id") String sortField,
                               @RequestParam(defaultValue = "asc") String sortDirection,
                               @RequestParam(value = "pageNo", defaultValue = "1") int pageNo){

        Page<Subject> page = subjectService.getFilteredAndSortedSubject(sortField, sortDirection, pageNo);

        List<Subject> subjects = page.getContent();

        String nextDir = sortDirection.equals("asc") ? "desc" : (sortDirection.equals("desc") ? "none" : "asc");

        model.addAttribute("subjects", subjects);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("nextDir", nextDir);

        return "subjects";
    }



}
