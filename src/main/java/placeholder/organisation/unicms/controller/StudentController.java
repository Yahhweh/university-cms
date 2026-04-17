package placeholder.organisation.unicms.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.service.GroupService;
import placeholder.organisation.unicms.service.StudentService;

@Controller
@RequestMapping("/students")
@PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
@AllArgsConstructor
public class StudentController {
    private static final String ASSIGN_STUDENT_GROUP = "Student has successfully assigned to group";

    private final StudentService studentService;
    private final GroupService groupService;

    @GetMapping()
    public String getStudents(Model model,
                              @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Student> page = studentService.findAll(pageable);

        model.addAttribute("students", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("url", "students");

        return "students";
    }

    @GetMapping("/profile")
    public String getProfile(
        Model model,
        Authentication authentication
    ) {
        Student student = studentService.findByEmail(authentication.getName());

        model.addAttribute("student", student);
        return "student-profile";
    }

    @GetMapping("/update-student-group")
    public String getAssignSubjectsForm(Model model,
                                        @RequestParam Long studentId) {
        model.addAttribute("groups", groupService.findAllGroups());
        studentService.findStudentDto(studentId)
            .ifPresent(dto -> model.addAttribute("student", dto));
        return "update-students-group";
    }

    @PostMapping("/update-student-group")
    public String assignStudent(@RequestParam Long studentId, @RequestParam Long groupId, RedirectAttributes redirectAttributes) {
        studentService.assignStudentToGroup(studentId, groupId);

        redirectAttributes.addFlashAttribute("successMessage", ASSIGN_STUDENT_GROUP);
        return "redirect:/students/update-student-group?studentId=" + studentId;
    }
}
