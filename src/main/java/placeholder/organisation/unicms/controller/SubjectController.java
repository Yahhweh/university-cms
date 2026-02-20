package placeholder.organisation.unicms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import placeholder.organisation.unicms.entity.Subject;
import placeholder.organisation.unicms.service.SubjectService;

import java.util.List;

@Controller
public class SubjectController {

    SubjectService subjectService;
    FieldExtractor fieldExtractor;

    public SubjectController(SubjectService subjectService, FieldExtractor fieldExtractor) {
        this.subjectService = subjectService;
        this.fieldExtractor = fieldExtractor;
    }

    @RequestMapping(path = "/subjects", method = RequestMethod.GET)
    public String getRoomTypes(Model model) {
        List<String> fields = fieldExtractor.getFieldNames(Subject.class);
        List<Subject> subjects = subjectService.findAllSubjects();

        model.addAttribute("fields", fields);
        model.addAttribute("objects", subjects);

        return "table";
    }

}
