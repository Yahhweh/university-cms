package placeholder.organisation.unicms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import placeholder.organisation.unicms.entity.Address;
import placeholder.organisation.unicms.entity.Person;
import placeholder.organisation.unicms.service.LecturerService;
import placeholder.organisation.unicms.service.StudentService;

import java.util.ArrayList;
import java.util.List;


@Controller
public class AddressController {

    StudentService studentService;
    LecturerService lecturerService;
    FieldExtractor fieldExtractor;

    public AddressController(StudentService studentService, LecturerService lecturerService, FieldExtractor fieldExtractor) {
        this.studentService = studentService;
        this.lecturerService = lecturerService;
        this.fieldExtractor = fieldExtractor;
    }

    @RequestMapping(path = "/addresses", method = RequestMethod.GET)
    public String getAddress(Model model){
        List<Address> studentAddresses = studentService.findAllStudents().stream()
                .map(Person::getAddress)
                .toList();

        List<Address> lecturerAddresses = lecturerService.findAllLecturers().stream()
                .map(Person::getAddress)
                .toList();

        List<Address> allAddresses = new ArrayList<>();
        allAddresses.addAll(studentAddresses);
        allAddresses.addAll(lecturerAddresses);

        List<String> fields =fieldExtractor.getFieldNames(Address.class);
        model.addAttribute("fields", fields);
        model.addAttribute("objects", allAddresses);

        return "table";
    }
}
