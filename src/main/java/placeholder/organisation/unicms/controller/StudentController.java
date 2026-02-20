package placeholder.organisation.unicms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.service.StudentService;

import java.util.List;

@Controller
public class StudentController {

    StudentService service;
    FieldExtractor fieldExtractor;

    public StudentController(StudentService service, FieldExtractor fieldExtractor) {
        this.service = service;
        this.fieldExtractor = fieldExtractor;
    }

    @RequestMapping(path = "/students", method = RequestMethod.GET)
     public String getStudents(Model model){
        List<Student> allStudents = service.findAllStudents();
        List<String> fields = fieldExtractor.getFieldNames(Student.class);
        model.addAttribute("objects", allStudents);
        model.addAttribute("fields", fields);
            return "table";
    }

}
