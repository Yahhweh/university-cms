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
                              @RequestParam(defaultValue = "asc") String sortDirection) {


        Page<Student> page = service.getFilteredAndSortedStudents(sortField, sortDirection);

        List<Student> students = page.getContent();

        String nextDir = sortDirection.equals("asc") ? "desc" : (sortDirection.equals("desc") ? "none" : "asc");

        model.addAttribute("students", students);
        model.addAttribute("currentSortField", sortField);
        model.addAttribute("currentSortDir", sortDirection);
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
