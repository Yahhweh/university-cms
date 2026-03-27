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
import placeholder.organisation.unicms.entity.Role;
import placeholder.organisation.unicms.entity.User;
import placeholder.organisation.unicms.service.LecturerService;
import placeholder.organisation.unicms.service.SubjectService;
import placeholder.organisation.unicms.service.UserService;
import placeholder.organisation.unicms.service.dto.request.filter.UserFilterRequestDTO;

import java.util.List;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
public class SharedController {

    private static final List<Role> STAFF_VISIBLE_ROLES = List.of(Role.STUDENT, Role.LECTURER);

    private final UserService userService;
    private final LecturerService lecturerService;
    private final SubjectService subjectService;

    public SharedController(UserService userService, LecturerService lecturerService, SubjectService subjectService) {
        this.userService = userService;
        this.lecturerService = lecturerService;
        this.subjectService = subjectService;
    }

    @GetMapping("/list")
    public String getUsers(Model model,
                           @PageableDefault(direction = Sort.Direction.ASC, sort = "id") Pageable pageable,
                           @ModelAttribute UserFilterRequestDTO requestDTO,
                           Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Page<User> pages;
        List<Role> availableRoles;
        if (isAdmin) {
            pages = userService.findAllFiltered(requestDTO, pageable);
            availableRoles = List.of(Role.values());
        } else {
            pages = userService.findAllFilteredByRoles(requestDTO, pageable, STAFF_VISIBLE_ROLES);
            availableRoles = STAFF_VISIBLE_ROLES;
        }

        model.addAttribute("users", pages.getContent());
        model.addAttribute("page", pages);
        model.addAttribute("url", "users/list");
        model.addAttribute("filters", requestDTO);
        model.addAttribute("roles", availableRoles);
        model.addAttribute("canManage", false);
        return "users";
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
        redirectAttributes.addFlashAttribute("successMessage", "You have successfully added subjects");
        return "redirect:/users/assign-subject";
    }

    @GetMapping("/remove-subject")
    public String getRemoveSubjectForm(@RequestParam(required = false) Long lecturerId, Model model) {
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
        for (Long subjectId : subjectsId) {
            lecturerService.removeSubjectFromLecturer(subjectId, lecturerId);
        }
        redirectAttributes.addFlashAttribute("successMessage", "You have successfully removed subject");
        return "redirect:/users/remove-subject";
    }
}