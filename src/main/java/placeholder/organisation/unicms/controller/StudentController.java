package placeholder.organisation.unicms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import placeholder.organisation.unicms.entity.PersonType;
import placeholder.organisation.unicms.service.LessonService;
import placeholder.organisation.unicms.service.StudentService;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping()
public class StudentController {
    LessonService lessonService;

    public StudentController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping("/smth")
    public String sayHello(Model model){
        model.addAttribute("text", lessonService.findByDate(LocalDate.of(2026, 01, 20), 14, PersonType.Student ));
        return "page_one";
    }

}
