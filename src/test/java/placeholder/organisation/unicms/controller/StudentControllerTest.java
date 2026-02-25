package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.service.StudentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Test
    void getLecturers_ShouldReturnViewName_whenEverythingIsCorrect() throws Exception {
        List<Student> students = List.of(new Student(), new Student());
        Page<Student> studentPage = new PageImpl<>(students);

        String sortField = "salary";
        String sortDir = "asc";
        int pageNo = 1;

        when(studentService.getFilteredAndSortedStudents(anyString(), anyString(), anyInt()))
                .thenReturn(studentPage);

        mockMvc.perform(get("/students")
                        .param("sortField", sortField)
                        .param("sortDirection", sortDir)
                        .param("pageNo", String.valueOf(pageNo)))
                .andExpect(status().isOk())
                .andExpect(view().name("students"))
                .andExpect(model().attribute("students", students))
                .andExpect(model().attribute("sortField", sortField))
                .andExpect(model().attribute("sortDir", sortDir))
                .andExpect(model().attribute("nextDir", "desc"))
                .andExpect(model().attributeExists("students", "sortField", "sortDir", "nextDir"));

        verify(studentService).getFilteredAndSortedStudents(sortField, sortDir, pageNo);
    }


    List<String> getPersonFields(){
        return List.of("id", "password", "name", "sureName", "gender", "email", "address", "dateOfBirth");
    }
}