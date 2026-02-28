package placeholder.organisation.unicms.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import placeholder.organisation.unicms.entity.Address;
import placeholder.organisation.unicms.entity.RoomType;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.service.StudentService;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Controller
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping(value = "/students")
    public String getStudents(Model model,
                              @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Student> page = service.findAll(pageable);

        model.addAttribute("students", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("url", "students");

        return "students";
    }
}
