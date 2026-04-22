package placeholder.organisation.unicms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.service.*;
import placeholder.organisation.unicms.service.dto.request.UpdateGroupInfoRequestDTO;
import placeholder.organisation.unicms.service.dto.request.filter.UserFilter;

import java.util.List;

@Controller
public class GroupController {

    private final GroupService groupService;
    private final StudentService studentService;
    private final UserService userService;
    private final LecturerService lecturerService;
    private final CourseService courseService;

    public GroupController(GroupService groupService, StudentService studentService, UserService userService, LecturerService lecturerService, CourseService courseService) {
        this.groupService = groupService;
        this.studentService = studentService;
        this.userService = userService;
        this.lecturerService = lecturerService;
        this.courseService = courseService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/groups")
    public String getGroups(Model model,
                            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Group> page = groupService.findAll(pageable);

        model.addAttribute("groups", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("url", "groups");

        return "groups";
    }

    @PreAuthorize("hasAnyRole('STUDENT', 'MENTOR')")
    @GetMapping("/group-profile")
    public String getStudentGroupProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Student student = studentService.findByEmail(userDetails.getUsername());
        List<Student> students = studentService.findStudentsRelatedToGroup(student.getGroup().getId());

        model.addAttribute("group", student.getGroup());
        model.addAttribute("students", students);
        return "group-profile";
    }

    @PreAuthorize("hasAnyRole('MENTOR', 'ADMIN', 'STAFF')")
    @PostMapping("/group-profile/update-info")
    public String updateGroupInfo(@ModelAttribute UpdateGroupInfoRequestDTO dto,
                                  RedirectAttributes redirectAttributes) {
        groupService.updateGroupInfo(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Group information updated successfully");
        return "redirect:/group-profile";
    }

    @PreAuthorize("hasRole('LECTURER')")
    @GetMapping("/{lecturerId}/groups")
    public String getLecturerGroups(Model model, @PathVariable Long lecturerId) {
        List<Group> groups = groupService.findGroupsRelatedToLecturer(lecturerId);
        model.addAttribute("groups", groups);
        model.addAttribute("lecturerId", lecturerId);
        model.addAttribute("url", lecturerId + "/groups");
        return "groups";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'MENTOR', 'LECTURER', 'STUDENT')")
    @GetMapping("/{groupId}/students")
    public String listStudentsRelatedToGroup(Model model, @ModelAttribute UserFilter filter,
                                             @PathVariable Long groupId) {
        List<Student> students = studentService.findStudentsRelatedToGroup(groupId, filter);
        model.addAttribute("users", students);
        model.addAttribute("url", groupId + "/students");
        model.addAttribute("groupId", groupId);
        model.addAttribute("filters", filter);

        return "list-students-group";
    }

}
