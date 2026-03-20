package placeholder.organisation.unicms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.service.*;
import placeholder.organisation.unicms.service.dto.request.FilterRequestDTO;
import placeholder.organisation.unicms.service.dto.request.LecturerRequestDTO;
import placeholder.organisation.unicms.service.dto.request.StudentRequestDTO;
import placeholder.organisation.unicms.service.dto.request.UserRequestDTO;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final GroupService groupService;
    private final SubjectService subjectService;
    private final StudentService studentService;
    private final LecturerService lecturerService;

    private final static String deleteMessage = "User has been successfully deleted";
    private final static String changeMessage = "User has been successfully changed";
    private final static String addUserMessage = "User has been successfully created";
    private final static String addStudentMessage = "Student has been successfully created";
    private final static String addLecturerMessage = "Lecturer has been successfully created";
    private final static String assignSubjectMessage = "You have successfully added subjects";
    private final static String removeSubjectMessage = "You have successfully removed subject";

    public AdminController(UserService userService, GroupService groupService,
                           SubjectService subjectService, StudentService studentService,
                           LecturerService lecturerService) {
        this.groupService = groupService;
        this.subjectService = subjectService;
        this.userService = userService;
        this.studentService = studentService;
        this.lecturerService = lecturerService;
    }

    @GetMapping
    public String showAdminPanel() {
        return "admin";
    }

    @PostMapping("/change-role")
    public String changeUserRole(@RequestParam Long id, @RequestParam String newRole,
                                 RedirectAttributes redirectAttributes,
                                 @PageableDefault(direction = Sort.Direction.ASC, sort = "id") Pageable pageable,
                                 @ModelAttribute FilterRequestDTO requestDTO) {
        Role enumRole = Role.valueOf(newRole.replace("ROLE_", ""));
        userService.changeRole(id, enumRole);
        addRedirectAttributes(changeMessage, pageable, requestDTO, redirectAttributes);
        return "redirect:/admin/users";
    }

    @GetMapping("/add-user")
    public String showAddUserForm(Model model) {
        model.addAttribute("groups", groupService.findAllGroups());
        model.addAttribute("subjects", subjectService.findAllSubjects());
        return "add-user";
    }

    @PostMapping("/add-user")
    public String addUser(@Valid @ModelAttribute UserRequestDTO userDTO, RedirectAttributes redirectAttributes) {
        userService.createUser(userDTO);
        redirectAttributes.addFlashAttribute("successMessage", addUserMessage);
        return "redirect:/admin/add-user";
    }

    @PostMapping("/add-student")
    public String addStudent(@Valid @ModelAttribute StudentRequestDTO studentDTO, RedirectAttributes redirectAttributes) {
        studentService.createStudent(studentDTO);
        redirectAttributes.addFlashAttribute("successMessage", addStudentMessage);
        return "redirect:/admin/add-user";
    }

    @PostMapping("/add-lecturer")
    public String addLecturer(@Valid @ModelAttribute LecturerRequestDTO lecturerDTO, RedirectAttributes redirectAttributes) {
        lecturerService.createLecturer(lecturerDTO);
        redirectAttributes.addFlashAttribute("successMessage", addLecturerMessage);
        return "redirect:/admin/add-user";
    }

    @GetMapping("/users")
    public String getUsers(Model model, @PageableDefault(direction = Sort.Direction.ASC, sort = "id") Pageable pageable,
                           @ModelAttribute FilterRequestDTO requestDTO) {
        Page<User> pages = userService.findAllFiltered(requestDTO, pageable);
        model.addAttribute("users", pages.getContent());
        model.addAttribute("page", pages);
        model.addAttribute("url", "admin/users");
        model.addAttribute("filters", requestDTO);
        model.addAttribute("roles", Role.values());
        return "users";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam Long id, RedirectAttributes redirectAttributes,
                             @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                             @ModelAttribute FilterRequestDTO requestDTO) {
        userService.deleteUser(id);
        addRedirectAttributes(deleteMessage, pageable, requestDTO, redirectAttributes);
        return "redirect:/admin/users";
    }

    @GetMapping("/assign-subject")
    public String getAssignSubjectForm(Model model) {
        model.addAttribute("lecturers", lecturerService.findAllLecturers());
        model.addAttribute("subjects", subjectService.findAllSubjects());
        return "assign-subject";
    }

    @PostMapping("/assign-subject")
    public String assignSubject(@RequestParam Long lecturerId,
                                @RequestParam List<Long> subjectsId,
                                RedirectAttributes redirectAttributes) {
        for (long subjectId : subjectsId) {
            lecturerService.assignSubjectToLecturer(subjectId, lecturerId);
        }
        redirectAttributes.addFlashAttribute("successMessage", assignSubjectMessage);
        return "redirect:/admin/assign-subject";
    }

    @GetMapping("/remove-subject")
    public String getRemoveSubjectForm(@RequestParam(required = false) Long lecturerId,
                                       Model model) {
        model.addAttribute("lecturers", lecturerService.findAllLecturers());
        if (lecturerId != null) {
            lecturerService.findLecturer(lecturerId).ifPresent(lecturer -> {
                model.addAttribute("selectedLecturer", lecturer);
                model.addAttribute("subjects", lecturer.getSubjects());
            });
        }
        return "remove-subject";
    }

    @PostMapping("/remove-subject")
    public String removeSubject(@RequestParam Long lecturerId,
                                @RequestParam List<Long> subjectsId,
                                RedirectAttributes redirectAttributes) {
        for (Long subjectId : subjectsId){
            lecturerService.removeSubjectFromLecturer(subjectId, lecturerId);
        }
        redirectAttributes.addFlashAttribute("successMessage", removeSubjectMessage);
        return "redirect:/admin/remove-subject";
    }

    private void addRedirectAttributes(String message, Pageable pageable, FilterRequestDTO requestDTO, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("successMessage", message);
        redirectAttributes.addAttribute("page", pageable.getPageNumber());
        pageable.getSort().forEach(order ->
            redirectAttributes.addAttribute("sort",
                order.getProperty() + "," + order.getDirection().name().toLowerCase())
        );
        redirectAttributes.addAttribute("name", requestDTO.getName() != null ? requestDTO.getName().toLowerCase() : "");
        redirectAttributes.addAttribute("sureName", requestDTO.getSureName() != null ? requestDTO.getSureName().toLowerCase() : "");
        redirectAttributes.addAttribute("email", requestDTO.getEmail() != null ? requestDTO.getEmail().toLowerCase() : "");
        redirectAttributes.addAttribute("role", requestDTO.getRole() != null ? requestDTO.getRole().toString() : "");
    }
}