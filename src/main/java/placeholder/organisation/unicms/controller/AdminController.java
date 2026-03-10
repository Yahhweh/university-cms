package placeholder.organisation.unicms.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.repository.GroupRepository;
import placeholder.organisation.unicms.service.GroupService;
import placeholder.organisation.unicms.service.LecturerService;
import placeholder.organisation.unicms.service.StudentService;
import placeholder.organisation.unicms.service.SubjectService;
import placeholder.organisation.unicms.service.dto.AddressDTO;
import placeholder.organisation.unicms.service.mapper.AddressMapper;

import java.security.PublicKey;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final StudentService studentService;
    private final LecturerService lecturerService;
    private final GroupService groupService;
    private final PasswordEncoder passwordEncoder;
    private final SubjectService subjectService;
    private final AddressMapper addressMapper;

    public AdminController(StudentService studentService, LecturerService lecturerService,
                           GroupService groupService, PasswordEncoder passwordEncoder,
                           SubjectService subjectService, AddressMapper addressMapper) {
        this.studentService = studentService;
        this.lecturerService = lecturerService;
        this.groupService = groupService;
        this.passwordEncoder = passwordEncoder;
        this.subjectService = subjectService;
        this.addressMapper = addressMapper;
    }

    @GetMapping
    public String showAdminPanel() {
        return "admin";
    }

    @GetMapping("/change-role")
    public String showChangeRoleForm(Model model) {
        model.addAttribute("students", studentService.findAllStudents());
        model.addAttribute("lecturers", lecturerService.findAllLecturers());
        return "change-role";
    }

    @PostMapping("/change-role")
    public String changeUserRole(@RequestParam Long id, @RequestParam String role) {
        Role enumRole = Role.valueOf(role.replace("ROLE_", ""));

        Optional<Student> student = studentService.findStudent(id);
        if (student.isPresent()) {
            student.get().setRole(enumRole);
            studentService.createStudent(student.get());
        } else {
            Lecturer lecturer = lecturerService.findLecturer(id).orElseThrow();
            lecturer.setRole(enumRole);
            lecturerService.createLecturer(lecturer);
        }
        return "redirect:/admin/change-role";
    }

    @GetMapping("/add-student")
    public String showAddStudentForm(Model model) {
        model.addAttribute("groups", groupService.findAllGroups());
        return "add-student";
    }

    @PostMapping("/add-student")
    public String addStudent(@RequestParam String name, @RequestParam String sureName, @RequestParam String password,
                             @RequestParam GenderType gender, @RequestParam LocalDate dateOfBirth,
                             @RequestParam Degree degree, @RequestParam String city, @RequestParam String street,
                             @RequestParam String phoneNumber, @RequestParam String houseNumber, @RequestParam String postalCode,
                             @RequestParam String country, @RequestParam(required = false) Long groupId) {
        Student student = new Student();

        Address address = getAddress(city, country, houseNumber, phoneNumber, street, postalCode);

        student.setName(name);
        student.setSureName(sureName);
        student.setEmail(name + "." + sureName + "@student.university.com");
        student.setPassword(passwordEncoder.encode(password));
        student.setGender(gender);
        student.setDateOfBirth(dateOfBirth);
        student.setDegree(degree);
        student.setAddress(address);
        student.setRole(Role.STUDENT);
        if (groupId != null) {
            student.setGroup(groupService.findGroup(groupId).get());
        }
        studentService.createStudent(student);
        return "redirect:/admin/add-student";
    }

    @GetMapping("/add-lecturer")
    public String showAddLecturerForm(Model model) {
        model.addAttribute("subjects", subjectService.findAllSubjects());
        return "add-lecturer";
    }

    @PostMapping("/add-lecturer")
    public String addLecturer(@RequestParam String name, @RequestParam String sureName, @RequestParam String password,
                              @RequestParam GenderType gender, @RequestParam LocalDate dateOfBirth,
                              @RequestParam String city, @RequestParam String street,
                              @RequestParam String phoneNumber, @RequestParam String houseNumber, @RequestParam String postalCode,
                              @RequestParam String country, @RequestParam Integer salary,
                              @RequestParam(required = false) List<Long> subjectIds) {
        Lecturer lecturer = new Lecturer();

        Address address = getAddress(city, country, houseNumber, phoneNumber, street, postalCode);

        lecturer.setAddress(address);
        lecturer.setName(name);
        lecturer.setSureName(sureName);
        lecturer.setEmail(name + "." + sureName + "@lecturer.university.com");
        lecturer.setPassword(passwordEncoder.encode(password));
        lecturer.setGender(gender);
        lecturer.setDateOfBirth(dateOfBirth);
        lecturer.setRole(Role.LECTURER);
        lecturer.setSalary(salary);
        if (subjectIds != null && !subjectIds.isEmpty()) {
            for (Long subjectId : subjectIds) {
                lecturer.getSubjects().add(subjectService.findSubject(subjectId).get());
            }
        }
        lecturerService.createLecturer(lecturer);

        return "redirect:/admin/add-lecturer";
    }

    private Address getAddress(String city, String country,
                               String houseNumber, String phoneNumber,
                               String street, String postalCode) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setCity(city);
        addressDTO.setCountry(country);
        addressDTO.setHouseNumber(houseNumber);
        addressDTO.setPhoneNumber(phoneNumber);
        addressDTO.setStreet(street);
        addressDTO.setPostalCode(postalCode);
        return addressMapper.toEntity(addressDTO);
    }

}