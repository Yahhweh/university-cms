package placeholder.organisation.unicms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.repository.PersonRepository;
import placeholder.organisation.unicms.service.*;
import placeholder.organisation.unicms.service.dto.request.LecturerRequestDTO;
import placeholder.organisation.unicms.service.dto.request.StudentRequestDTO;
import placeholder.organisation.unicms.service.dto.response.AddressResponseDTO;
import placeholder.organisation.unicms.service.dto.response.LecturerResponseDTO;
import placeholder.organisation.unicms.service.dto.response.StudentResponseDTO;
import placeholder.organisation.unicms.service.mapper.AddressMapper;
import placeholder.organisation.unicms.service.mapper.LecturerMapper;

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
                                     @RequestParam(required = false) String name,
                                     @RequestParam(required = false) String sureName,
                                     @RequestParam(required = false) String email,
                                     @RequestParam(required = false) Role role) {

        Page<Person> page = personService.findAllFiltered(name, sureName, email, role, pageable);
        model.addAttribute("page", page);
        model.addAttribute("persons", page.getContent());
        model.addAttribute("url", "admin/change-role");
        model.addAttribute("roles", Role.values());
        model.addAttribute("filterName", name);
        model.addAttribute("filterSureName", sureName);
        model.addAttribute("filterEmail", email);
        model.addAttribute("filterRole", role);
        return "change-role";
    }

    @PostMapping("/change-role")
    public String changeUserRole(@RequestParam Long id, @RequestParam String role) {
        Role enumRole = Role.valueOf(role.replace("ROLE_", ""));
        personService.changeRole(id, enumRole);
        return "redirect:/admin/change-role";
    }

    @GetMapping("/add-student")
    public String showAddStudentForm(Model model) {
        model.addAttribute("groups", groupService.findAllGroups());
        return "add-student";
    }

    @PostMapping("/add-student")
    public String addStudent(@Valid @ModelAttribute StudentRequestDTO studentDTO) {
        studentService.createStudent(studentDTO);
        return "redirect:/admin/add-student";
    }

    @GetMapping("/add-lecturer")
    public String showAddLecturerForm(Model model) {
        model.addAttribute("subjects", subjectService.findAllSubjects());
        return "add-lecturer";
    }

    @PostMapping("/add-lecturer")
    public String addLecturer(@Valid @ModelAttribute LecturerRequestDTO lecturerDTO) {
        lecturerService.createLecturer(lecturerDTO);
        return "redirect:/admin/add-lecturer";
    }
}