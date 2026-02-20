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
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

        assertThat(initial.getName()).isEqualTo("Jane");
        assertThat(initial.getDegree()).isEqualTo(Degree.Master);
    }

    @Test
    void updateStudent_whenValidStudentDTO_thanStudentIsUpdated() {
        Student initial = getStudent();
        StudentDTO changes = getStudentDto();
        long id = initial.getId();

        when(studentRepository.findById(id)).thenReturn(Optional.of(initial));

        studentService.updateStudent(id, changes);

        verify(studentMapper).updateEntityFromDto(changes, initial);
        verify(studentRepository).save(initial);

        assertThat(initial.getName()).isEqualTo("Jane");
    }

    @Test
    void createStudent_shouldSave_whenCorrectStudentGiven() {
        Student student = getStudent();

        assertDoesNotThrow(() -> studentService.createStudent(student));

        verify(studentRepository).save(student);
    }

    @Test
    void removeStudent_shouldRemoveStudent_WhenStudentExists() {
        Student student = getStudent();

        when(studentRepository.existsById(student.getId())).thenReturn(true);

        studentService.removeStudent(student.getId());

        verify(studentRepository).deleteById(student.getId());
    }

    @Test
    void removeStudent_shouldThrowEntityNotFound_WhenStudentDoesNotExist() {
        long id = 22L;

        when(studentRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> studentService.removeStudent(id));
        verify(studentRepository).existsById(id);
    }

    private Student getStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setName("Bob");
        student.setSureName("SureName");
        student.setDegree(Degree.Bachelor);
        return student;
    }

    private StudentDTO getStudentDto() {
        StudentDTO dto = new StudentDTO();
        dto.setName("Jane");
        dto.setSureName("Doe");
        dto.setDegree(Degree.Master);
        return dto;
    }
}