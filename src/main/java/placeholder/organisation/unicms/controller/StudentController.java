package placeholder.organisation.unicms.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import placeholder.organisation.unicms.entity.Address;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.service.StudentService;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Controller
public class StudentController {

    StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping("/students")
    public String getStudents(Model model,
                              @RequestParam(defaultValue = "id") String sortField,
                              @RequestParam(defaultValue = "asc") String sortDirection,
                              @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {

        Page<Student> page = service.getFilteredAndSortedStudents(sortField, sortDirection, pageNo);

        String nextDir = sortDirection.equals("asc") ? "desc" : "asc";

        model.addAttribute("students", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDirection);
        model.addAttribute("nextDir", nextDir);

        return "students";
    }

    private final List<String> getAddressFields(){
        List<Field> fields = Arrays.stream(Address.class.getDeclaredFields()).toList();
        return fields.stream()
                .map(Field::getName)
                .toList();

    }
}
