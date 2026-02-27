package placeholder.organisation.unicms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.entity.Subject;
import placeholder.organisation.unicms.service.SubjectService;

import java.util.List;

@Controller
public class SubjectController {

    private final  SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @RequestMapping(path = "/subjects", method = RequestMethod.GET)
    public String getRoomTypes(Model model,
                               @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Subject> page = subjectService.findAll(pageable);

        model.addAttribute("subjects", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("url", "subjects");


        return "subjects";
    }


}
