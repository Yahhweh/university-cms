package placeholder.organisation.unicms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.service.CourseService;
import placeholder.organisation.unicms.service.LecturerService;
import placeholder.organisation.unicms.service.SubjectService;
import placeholder.organisation.unicms.service.UserService;
import placeholder.organisation.unicms.service.dto.request.filter.UserFilter;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
public class UserController {

    private final UserService userService;
    private final LecturerService lecturerService;
    private final SubjectService subjectService;
    private final CourseService courseService;

    public UserController(UserService userService, LecturerService lecturerService, SubjectService subjectService,
                          CourseService courseService) {
        this.userService = userService;
        this.lecturerService = lecturerService;
        this.subjectService = subjectService;
        this.courseService = courseService;
    }

    @GetMapping()
    public String getUsers(Model model,
                           @PageableDefault(direction = Sort.Direction.ASC, sort = "id") Pageable pageable,
                           @ModelAttribute UserFilter requestDTO,
                           Authentication auth) {

        boolean isAdmin = auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Page<User> users = userService.findUsersForAccess(requestDTO, pageable, isAdmin);

        model.addAttribute("users", users.getContent());
        model.addAttribute("page", users);
        model.addAttribute("url", "users/list");
        model.addAttribute("filters", requestDTO);
        model.addAttribute("canManage", false);
        return "users";
    }

    @GetMapping("/update-lecturer-subject")
    public String getAssignSubjectForm(Model model,
                                       @RequestParam(required = false) Long lecturerId) {
        model.addAttribute("lecturers", lecturerService.findAllLecturers());
        model.addAttribute("subjects", subjectService.findAllSubjects());
        model.addAttribute("lecturerSubjects", new HashSet<>());
        if(lecturerId != null){
            lecturerService.findLecturer(lecturerId).ifPresent(lecturer ->  {
                model.addAttribute("selectedLecturer", lecturerId);
                model.addAttribute("lecturerSubjects",
                    lecturer.getSubjects().stream()
                        .map(Subject::getId)
                        .collect(Collectors.toSet()));
            });
        }
        return "update-course-group";
    }

    @PostMapping("/update-lecturer-subject")
    public String updateLecturerSubject(@RequestParam Long lecturerId,
                                @RequestParam List<Long> subjectIds,
                                RedirectAttributes redirectAttributes) {
        lecturerService.updateLecturerSubjects(subjectIds, lecturerId);
        redirectAttributes.addFlashAttribute("successMessage", "You have successfully updated subjects");
        return "redirect:/users/update-lecturer-subject";
    }


    @GetMapping("/courses")
    public String getCourses(Model model,
                             @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                             @RequestParam(required = false) String name) {
        Page<Course> page = courseService.findAll(pageable, name);
        model.addAttribute("courses", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("url", "users/courses");
        model.addAttribute("name", name);
        return "courses";
    }
}