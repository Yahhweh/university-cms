package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.service.LecturerService;
import placeholder.organisation.unicms.service.StudentService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(AddressController.class)
class AddressControllerTest {

    @MockitoBean
    StudentService studentService;
    @MockitoBean
    LecturerService lecturerService;
    @MockitoBean
    FieldExtractor fieldExtractor;
    @Autowired
    MockMvc mockMvc;

    @Test
    void getAddress_ShouldReturnViewName_whenEverythingIsCorrect() throws Exception {
        List<String> fields = List.of("id", "city", "street", "phoneNumber", "country", "houseNumber", "postalCode");

        Address address1 = new Address();
        Address address2 = new Address();

        Student student1 = new Student();
        student1.setAddress(address1);

        Lecturer lecturer1 = new Lecturer();
        lecturer1.setAddress(address2);

        List<Student> students = List.of(student1);
        List<Lecturer> lecturers = List.of(lecturer1);

        List<Address> expectedAddresses = List.of(address1, address2);

        when(fieldExtractor.getFieldNames(Address.class)).thenReturn(fields);
        when(studentService.findAllStudents()).thenReturn(students);
        when(lecturerService.findAllLecturers()).thenReturn(lecturers);

        mockMvc.perform(get("/addresses"))
                .andExpect(status().isOk())
                .andExpect(view().name("table"))
                .andExpect(model().attribute("fields", fields))
                .andExpect(model().attribute("objects", expectedAddresses));

        verify(fieldExtractor).getFieldNames(Address.class);
        verify(studentService).findAllStudents();
        verify(lecturerService).findAllLecturers();
    }
}