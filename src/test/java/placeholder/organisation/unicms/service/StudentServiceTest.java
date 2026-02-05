package placeholder.organisation.unicms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.Degree;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.repository.StudentRepository;
import placeholder.organisation.unicms.service.dto.StudentDTO;
import placeholder.organisation.unicms.service.mapper.StudentMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Spy
    private StudentMapper studentMapper = Mappers.getMapper(StudentMapper.class);

    @InjectMocks
    private StudentService studentService;

    @Test
    void updateStudent_whenValidStudentDTO_thenStudentIsUpdated() {
        Student initial = getStudent();
        StudentDTO changes = getStudentDto();
        long id = initial.getId();

        when(studentRepository.findById(id)).thenReturn(Optional.of(initial));

        studentService.updateStudent(id, changes);

        verify(studentMapper).updateEntityFromDto(changes, initial);
        verify(studentRepository).save(initial);

        assertThat(initial.getName()).isEqualTo("Ivan");
        assertThat(initial.getDegree()).isEqualTo(Degree.Master);
    }

    private Student getStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setName("Petr");
        student.setSureName("Petrov");
        student.setDegree(Degree.Bachelor);
        return student;
    }

    private StudentDTO getStudentDto() {
        StudentDTO dto = new StudentDTO();
        dto.setName("Ivan");
        dto.setSureName("Ivanov");
        dto.setDegree(Degree.Master);
        return dto;
    }
}