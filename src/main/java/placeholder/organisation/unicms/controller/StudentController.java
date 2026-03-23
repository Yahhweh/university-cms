package placeholder.organisation.unicms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.service.StudentService;

@Controller
@RequestMapping("/students")
@PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping()
    public String getStudents(Model model,
                              @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Student> page = service.findAll(pageable);

        model.addAttribute("students", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("url", "students");

        return "students";
    }

    @GetMapping(value = "/profile")
    public String getProfile(
        Model model,
        Authentication authentication
    ){
        Student student = service.findByEmail(authentication.getName());

        model.addAttribute("student", student);
        return "student-profile";
    }
}
