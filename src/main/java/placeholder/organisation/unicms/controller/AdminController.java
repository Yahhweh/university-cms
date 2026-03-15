package placeholder.organisation.unicms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.service.*;
import placeholder.organisation.unicms.service.dto.request.FilterRequestDTO;
import placeholder.organisation.unicms.service.dto.request.LecturerRequestDTO;
import placeholder.organisation.unicms.service.dto.request.StudentRequestDTO;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final PersonService personService;
    private final GroupService groupService;
    private final SubjectService subjectService;
    private final StudentService studentService;
    private final LecturerService lecturerService;

    public AdminController(PersonService personService, GroupService groupService,
                           SubjectService subjectService, StudentService studentService,
                           LecturerService lecturerService) {
        this.groupService = groupService;
        this.subjectService = subjectService;
        this.personService = personService;
        this.studentService = studentService;
        this.lecturerService = lecturerService;
    }

    @GetMapping
    public String showAdminPanel() {
        return "admin";
    }

    @GetMapping("/change-role")
    public String showChangeRoleForm(Model model,
                                     @PageableDefault(direction = Sort.Direction.ASC, sort = "id") Pageable pageable,
                                     @ModelAttribute FilterRequestDTO filterRequestDTO) {

        Page<Person> page = personService.findAllFiltered(filterRequestDTO, pageable);
        model.addAttribute("page", page);
        model.addAttribute("persons", page.getContent());
        model.addAttribute("url", "admin/change-role");
        model.addAttribute("roles", Role.values());
        model.addAttribute("filters", filterRequestDTO);
        return "change-role";
    }

    @PostMapping("/change-role")
    public String changeUserRole(@RequestParam Long id, @RequestParam String role,
                                 Model model) {
        Role enumRole = Role.valueOf(role.replace("ROLE_", ""));
        personService.changeRole(id, enumRole);
        model.addAttribute("subtitle", "Successfully changed Role");
        model.addAttribute("message", "User`s role has been successfully changed");
        model.addAttribute("returnUrl", "/admin/change-role");
        model.addAttribute("returnLabel", "Go to table");
        return "success";
    }

    @GetMapping("/add-student")
    public String showAddStudentForm(Model model) {
        model.addAttribute("groups", groupService.findAllGroups());
        return "add-student";
    }

    @PostMapping("/add-student")
    public String addStudent(@Valid @ModelAttribute StudentRequestDTO studentDTO, Model model) {
        studentService.createStudent(studentDTO);
        model.addAttribute("subtitle", "Successfully created");
        model.addAttribute("message", studentDTO.getName());
        model.addAttribute("returnUrl", "/admin/add-student");
        model.addAttribute("returnLabel", "Go to table");
        return "success";
    }

    @GetMapping("/add-lecturer")
    public String showAddLecturerForm(Model model) {
        model.addAttribute("subjects", subjectService.findAllSubjects());
        return "add-lecturer";
    }

    @PostMapping("/add-lecturer")
    public String addLecturer(@Valid @ModelAttribute LecturerRequestDTO lecturerDTO, Model model) {
        lecturerService.createLecturer(lecturerDTO);
        model.addAttribute("subtitle", "Successfully created");
        model.addAttribute("message", lecturerDTO.getName());
        model.addAttribute("returnUrl", "/admin/add-student");
        model.addAttribute("returnLabel", "Go to table");
        return "success";
    }

    @GetMapping("/list-users")
    public String getListUsers(Model model, @PageableDefault(direction = Sort.Direction.ASC, sort = "id") Pageable pageable,
                               @ModelAttribute FilterRequestDTO requestDTO){
        Page<Person> pageList = personService.findAllFiltered(requestDTO, pageable);
        model.addAttribute("persons", pageList.getContent());
        model.addAttribute("page", pageList);
        model.addAttribute("url", "admin/list-users");
        model.addAttribute("filters", requestDTO);
        return "list-users";
    }

    @PostMapping("/delete-person")
    public String deleteUser(@RequestParam Long id, Model model){
        personService.deletePerson(id);
        model.addAttribute("subtitle", "Successfully deleted");
        model.addAttribute("message", "User has been successfully deleted");
        model.addAttribute("returnUrl", "/admin/change-role");
        model.addAttribute("returnLabel", "Go to table");
        return "success";
    }


    @GetMapping("/assign-subject")
    public String getAssignSubjectForm(Model model) {
        model.addAttribute("lecturers", lecturerService.findAllLecturers());
        model.addAttribute("subjects", subjectService.findAllSubjects());
        return "assign-subject";
    }

    @PostMapping("/assign-subject")
    public String assignSubject(@RequestParam Long lecturerId,
                                @RequestParam Long subjectId,
                                Model model) {
        lecturerService.assignSubjectToLecturer(subjectId, lecturerId);
        model.addAttribute("subtitle", "Successfully assigned");
        model.addAttribute("message", "Subject assigned to lecturer");
        model.addAttribute("returnUrl", "/admin/assign-subject");
        model.addAttribute("returnLabel", "Assign more");
        return "success";
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
                                @RequestParam Long subjectId,
                                Model model) {
        lecturerService.removeSubjectFromLecturer(subjectId, lecturerId);
        model.addAttribute("subtitle", "Successfully removed");
        model.addAttribute("message", "Subject removed from lecturer");
        model.addAttribute("returnUrl", "/admin/remove-subject");
        model.addAttribute("returnLabel", "Remove more");
        return "success";
    }
}