package placeholder.organisation.unicms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.repository.LecturerRepository;
import placeholder.organisation.unicms.repository.SubjectRepository;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.entity.Subject;
import placeholder.organisation.unicms.service.dto.request.LecturerRequestDTO;
import placeholder.organisation.unicms.service.dto.response.LecturerResponseDTO;
import placeholder.organisation.unicms.service.mapper.LecturerMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LecturerServiceTest {

    @Mock
    private LecturerRepository lecturerRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Spy
    LecturerMapper lecturerMapper = Mappers.getMapper(LecturerMapper.class);

    @InjectMocks
    private LecturerService lecturerService;

    @Test
    void assignSubjectToLecturer_callsRepositorySave_whenSubjectAndLecturerExist() {
        long subjectId = 1L;
        long lecturerId = 2L;

        Subject subject = new Subject();
        Lecturer lecturer = new Lecturer();
        lecturer.setSubjects(new HashSet<>());

        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));
        when(lecturerRepository.findById(lecturerId)).thenReturn(Optional.of(lecturer));

        lecturerService.updateLecturerSubjects(List.of(subjectId), lecturerId);

        assertTrue(lecturer.getSubjects().contains(subject));
        verify(subjectRepository).findById(subjectId);
        verify(lecturerRepository).findById(lecturerId);
    }

    @Test
    void assignSubjectToLecturer_throwsServiceException_whenDaoExceptionOccurs() {
        long subjectId = 1L;
        long lecturerId = 2L;

        Lecturer lecturer = new Lecturer();
        lecturer.setSubjects(new HashSet<>());

        when(lecturerRepository.findById(lecturerId)).thenReturn(Optional.of(lecturer));
        when(subjectRepository.findById(subjectId)).thenThrow(new EntityNotFoundException(javax.security.auth.Subject.class, "1"));

        assertThrows(EntityNotFoundException.class, () ->
                lecturerService.updateLecturerSubjects(List.of(subjectId), lecturerId)
        );
    }

    @Test
    void updateLecturer_whenValidLecturerDTO_thanLecturerIsUpdated() {
        Lecturer initial = getLecturer();
        LecturerRequestDTO changes = getLecturerDto();
        long id = initial.getId();

        when(lecturerRepository.findById(id)).thenReturn(Optional.of(initial));

        lecturerService.updateLecturer(id, changes);

        verify(lecturerMapper).updateEntityFromDto(changes, initial);
        verify(lecturerRepository).save(initial);

        assertThat(initial.getName()).isEqualTo("Jane");
        assertThat(initial.getSalary()).isEqualTo(55000);
    }

    @Test
    void createLecturer_shouldSave_whenCorrectLecturerGiven() {
        Lecturer lecturer = getLecturer();

        assertDoesNotThrow(() -> lecturerService.createLecturer(lecturer));

        verify(lecturerRepository).save(lecturer);
    }

    @Test
    void removeLecturer_shouldRemoveLecturer_WhenLecturerExists() {
        Lecturer lecturer = getLecturer();

        when(lecturerRepository.existsById(lecturer.getId())).thenReturn(true);

        lecturerService.removeLecturer(lecturer.getId());

        verify(lecturerRepository).deleteById(lecturer.getId());
    }

    @Test
    void removeLecturer_shouldThrowEntityNotFound_WhenLecturerDoesNotExist() {
        long id = 22L;

        when(lecturerRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> lecturerService.removeLecturer(id));
        verify(lecturerRepository).existsById(id);
    }

    Lecturer getLecturer() {
        Lecturer lecturer = new Lecturer();
        lecturer.setId(1L);
        lecturer.setName("John");
        lecturer.setSureName("Doe");
        lecturer.setSalary(40000);
        return lecturer;
    }

    LecturerRequestDTO getLecturerDto() {
        LecturerRequestDTO dto = new LecturerRequestDTO();
        dto.setName("Jane");
        dto.setSureName("Doe");
        dto.setSalary(55000);
        return dto;
    }
}