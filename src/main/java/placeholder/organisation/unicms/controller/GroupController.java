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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.service.GroupService;
import placeholder.organisation.unicms.service.StudentService;
import placeholder.organisation.unicms.service.dto.request.UpdateGroupInfoRequestDTO;

import java.util.List;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class GroupController {

    private final GroupService groupService;
    private final StudentService studentService;

    public GroupController(GroupService groupService, StudentService studentService) {
        this.groupService = groupService;
        this.studentService = studentService;
    }

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

    @PreAuthorize("hasAnyRole('MENTOR', 'ADMIN')")
    @PostMapping("/group-profile/update-info")
    public String updateGroupInfo(@ModelAttribute UpdateGroupInfoRequestDTO dto,
                                  RedirectAttributes redirectAttributes) {
        groupService.updateGroupInfo(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Group information updated successfully");
        return "redirect:/group-profile";
    }

}
