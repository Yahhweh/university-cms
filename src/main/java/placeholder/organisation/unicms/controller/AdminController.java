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
import placeholder.organisation.unicms.service.dto.request.filter.UserFilter;
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

    private static final String DELETE_MESSAGE = "User has been successfully deleted";
    private static final String CHANGE_MESSAGE = "User has been successfully changed";
    private static final String CREATE_USER_MESSAGE = "User has been successfully created";
    private static final String CREATE_STUDENT_MESSAGE = "Student has been successfully created";
    private static final String CREATE_LECTURER_MESSAGE = "Lecturer has been successfully created";
    private static final String ASSIGN_GROUP_MESSAGE = "Groups have been successfully assigned";
    private static final String REMOVE_GROUP_MESSAGE = "Group has been successfully removed from course";

    private final UserService userService;
    private final GroupService groupService;
    private final SubjectService subjectService;
    private final StudentService studentService;
    private final LecturerService lecturerService;
    private final LessonService lessonService;
    private final CourseService courseService;

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
                                 @ModelAttribute UserFilter filter) {
        Role enumRole = Role.valueOf(newRole.replace("ROLE_", ""));
        userService.changeRole(id, enumRole);
        RedirectAttributesHelper.addPageAndFilterAttributes(CHANGE_MESSAGE, pageable, redirectAttributes, filter);
        return "redirect:/admin/users";
    }

    @GetMapping("/create-user")
    public String showCreateUserForm(Model model) {
        model.addAttribute("groups", groupService.findAllGroups());
        model.addAttribute("subjects", subjectService.findAllSubjects());
        return "create-user";
    }


    @PostMapping("/create-user")
    public String createUser(@Valid @ModelAttribute UserRequestDTO userDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "create-user";
        }
        userService.createUser(userDTO);
        redirectAttributes.addFlashAttribute("successMessage", CREATE_USER_MESSAGE);
        return "redirect:/admin/create-user";
    }

    @PostMapping("/create-student")
    public String createStudent(@Valid @ModelAttribute StudentRequestDTO studentDTO, RedirectAttributes redirectAttributes) {
        studentService.createStudent(studentDTO);
        redirectAttributes.addFlashAttribute("successMessage", CREATE_STUDENT_MESSAGE);
        return "redirect:/admin/create-user";
    }

    @PostMapping("/create-lecturer")
    public String createLecturer(@Valid @ModelAttribute LecturerRequestDTO lecturerDTO, RedirectAttributes redirectAttributes) {
        lecturerService.createLecturer(lecturerDTO);
        redirectAttributes.addFlashAttribute("successMessage", CREATE_LECTURER_MESSAGE);
        return "redirect:/admin/create-user";
    }

    @GetMapping("/users")
    public String getUsers(Model model, @PageableDefault(direction = Sort.Direction.ASC, sort = "id") Pageable pageable,
                           @ModelAttribute UserFilter filter) {
        Page<User> pages = userService.findAllFiltered(filter, pageable);
        model.addAttribute("users", pages.getContent());
        model.addAttribute("page", pages);
        model.addAttribute("url", "admin/users");
        model.addAttribute("filters", filter);
        model.addAttribute("roles", Role.values());
        return "users";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam Long id, RedirectAttributes redirectAttributes,
                             @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                             @ModelAttribute UserFilter filter) {
        userService.deleteUser(id);
        RedirectAttributesHelper.addPageAndFilterAttributes(CHANGE_MESSAGE, pageable, redirectAttributes, filter);
        return "redirect:/admin/users";
    }

    @GetMapping("/update-course-groups")
    public String getUpdateCourseGroupsForm(Model model, @RequestParam(required = false) Long courseId) {
        model.addAttribute("courses", courseService.findAllCourses());
        model.addAttribute("groups", groupService.findAllGroups());
        if (courseId != null) {
            courseService.findCourse(courseId).ifPresent(course -> {
                model.addAttribute("selectedCourse", course);
                model.addAttribute("courseGroups", groupService.getGroupsByCourse(courseId));
            });
        }
        return "update-course-groups";
    }

    @PostMapping("/update-course-groups")
    public String updateCourseGroups(@RequestParam Long courseId,
                                     @RequestParam List<Long> groupIds,
                                     RedirectAttributes redirectAttributes) {

        groupService.updateCourseGroups(courseId, groupIds);
        redirectAttributes.addFlashAttribute("successMessage", ASSIGN_GROUP_MESSAGE);
        return "redirect:/admin/update-course-groups";
    }
}