package placeholder.organisation.unicms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.service.LecturerService;

import java.util.List;

@Controller
public class LecturerController {

    LecturerService lecturerService;
    FieldExtractor fieldExtractor;

    public LecturerController(LecturerService lecturerService, FieldExtractor fieldExtractor) {
        this.lecturerService = lecturerService;
        this.fieldExtractor = fieldExtractor;
    }

    @RequestMapping(value = "/lecturers", method = RequestMethod.GET)
    public String getLecturers(Model model){
        List<String> fields = fieldExtractor.getFieldNames(Lecturer.class);
        List<Lecturer> lecturers = lecturerService.findAllLecturers();
        model.addAttribute("fields", fields);
        model.addAttribute("objects", lecturers);
        return "table";
    }
}
