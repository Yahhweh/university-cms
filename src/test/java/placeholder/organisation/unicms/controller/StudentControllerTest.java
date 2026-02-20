package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.service.StudentService;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    StudentService studentService;
    @MockitoBean
    FieldExtractor fieldExtractor;

    @Test
    void getLecturers_ShouldReturnViewName_whenEverythingIsCorrect() throws Exception {
        List<String> fields = new java.util.ArrayList<>(List.of("degree", "group"));
        fields.addAll(getPersonFields());
        List<Student> students = List.of(new Student(), new Student());

        when(fieldExtractor.getFieldNames(Student.class)).thenReturn(fields);
        when(studentService.findAllStudents()).thenReturn(students);

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("table"))
                .andExpect(model().attribute("fields", fields))
                .andExpect(model().attribute("objects", students))
                .andExpect(model().attributeExists("fields", "objects"));

        verify(fieldExtractor).getFieldNames(Student.class);
        verify(studentService).findAllStudents();
    }


    List<String> getPersonFields(){
        return List.of("id", "password", "name", "sureName", "gender", "email", "address", "dateOfBirth");
    }
}