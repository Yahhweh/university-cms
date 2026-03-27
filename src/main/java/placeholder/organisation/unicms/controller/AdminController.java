package placeholder.organisation.unicms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.service.*;
import placeholder.organisation.unicms.service.dto.request.filter.UserFilterRequestDTO;
import placeholder.organisation.unicms.service.CourseService;
import placeholder.organisation.unicms.service.dto.request.LecturerRequestDTO;
import placeholder.organisation.unicms.service.dto.request.StudentRequestDTO;
import placeholder.organisation.unicms.service.dto.request.UserRequestDTO;

import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@Validated
public class AdminController {

    private final UserService userService;
    private final GroupService groupService;
    private final SubjectService subjectService;
    private final StudentService studentService;
    private final LecturerService lecturerService;
    private final LessonService lessonService;
    private final CourseService courseService;

    private final static String deleteMessage = "User has been successfully deleted";
    private final static String changeMessage = "User has been successfully changed";
    private final static String addUserMessage = "User has been successfully created";
    private final static String addStudentMessage = "Student has been successfully created";
    private final static String addLecturerMessage = "Lecturer has been successfully created";
    private final static String assignGroupMessage = "Groups have been successfully assigned";
    private final static String removeGroupMessage = "Group has been successfully removed from course";

    public AdminController(UserService userService, GroupService groupService,
                           SubjectService subjectService, StudentService studentService,
                           LecturerService lecturerService, LessonService lessonService,
                           CourseService courseService) {
        this.groupService = groupService;
        this.subjectService = subjectService;
        this.userService = userService;
        this.studentService = studentService;
        this.lecturerService = lecturerService;
        this.lessonService = lessonService;
        this.courseService = courseService;
    }

    @GetMapping
    public String showAdminPanel() {
        return "admin";
    }

    @PostMapping("/change-role")
    public String changeUserRole(@RequestParam Long id, @RequestParam String newRole,
                                 RedirectAttributes redirectAttributes,
                                 @PageableDefault(direction = Sort.Direction.ASC, sort = "id") Pageable pageable,
                                 @ModelAttribute UserFilterRequestDTO requestDTO) {
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
    public String addUser(@Valid @ModelAttribute UserRequestDTO userDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            return "add-user";
        }
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
                           @ModelAttribute UserFilterRequestDTO requestDTO) {
        Page<User> pages = userService.findAllFiltered(requestDTO, pageable);
        model.addAttribute("users", pages.getContent());
        model.addAttribute("page", pages);
        model.addAttribute("url", "admin/users");
        model.addAttribute("filters", requestDTO);
        model.addAttribute("roles", Role.values());
        model.addAttribute("canManage", true);
        return "users";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam Long id, RedirectAttributes redirectAttributes,
                             @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                             @ModelAttribute UserFilterRequestDTO requestDTO) {
        userService.deleteUser(id);
        addRedirectAttributes(deleteMessage, pageable, requestDTO, redirectAttributes);
        return "redirect:/admin/users";
    }

    @GetMapping("/assign-group")
    public String getAssignGroupForm(Model model) {
        model.addAttribute("courses", courseService.findAllCourses());
        model.addAttribute("groups", groupService.findAllGroups());
        return "assign-group";
    }

    @PostMapping("/assign-group")
    public String assignGroup(@RequestParam Long courseId,
                              @RequestParam List<Long> groupIds,
                              RedirectAttributes redirectAttributes) {
        groupService.assignGroupsToCourse(courseId, groupIds);
        redirectAttributes.addFlashAttribute("successMessage", assignGroupMessage);
        return "redirect:/admin/assign-group";
    }

    @GetMapping("/remove-group")
    public String getRemoveGroupForm(@RequestParam(required = false) Long courseId, Model model) {
        model.addAttribute("courses", courseService.findAllCourses());
        if (courseId != null) {
            courseService.findAllCourses().stream()
                    .filter(c -> c.getId().equals(courseId))
                    .findFirst()
                    .ifPresent(course -> {
                        model.addAttribute("selectedCourse", course);
                        model.addAttribute("groups", groupService.findGroupsByCourse(courseId));
                    });
        }
        return "remove-group";
    }

    @PostMapping("/remove-group")
    public String removeGroup(@RequestParam List<Long> groupIds,
                              RedirectAttributes redirectAttributes) {
        for (Long groupId : groupIds) {
            groupService.removeGroupFromCourse(groupId);
        }
        redirectAttributes.addFlashAttribute("successMessage", removeGroupMessage);
        return "redirect:/admin/remove-group";
    }

    private void addRedirectAttributes(String message, Pageable pageable, UserFilterRequestDTO requestDTO, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("successMessage", message);
        redirectAttributes.addAttribute("page", pageable.getPageNumber());
        pageable.getSort().forEach(order ->
            redirectAttributes.addAttribute("sort",
                order.getProperty() + "," + order.getDirection().name().toLowerCase())
        );
        redirectAttributes.addAttribute("duration", requestDTO.getName() != null ? requestDTO.getName().toLowerCase() : "");
        redirectAttributes.addAttribute("sureName", requestDTO.getSureName() != null ? requestDTO.getSureName().toLowerCase() : "");
        redirectAttributes.addAttribute("email", requestDTO.getEmail() != null ? requestDTO.getEmail().toLowerCase() : "");
        redirectAttributes.addAttribute("role", requestDTO.getRole() != null ? requestDTO.getRole().toString() : "");
    }
}