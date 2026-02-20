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
import placeholder.organisation.unicms.service.dto.LecturerDTO;
import placeholder.organisation.unicms.service.mapper.LecturerMapper;

import java.util.HashSet;
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

        lecturerService.assignSubjectToLecturer(subjectId, lecturerId);

        assertTrue(lecturer.getSubjects().contains(subject));
        verify(subjectRepository).findById(subjectId);
        verify(lecturerRepository).findById(lecturerId);
    }

    @Test
    void assignSubjectToLecturer_throwsServiceException_whenDaoExceptionOccurs() {
        long subjectId = 1L;
        long lecturerId = 2L;

        when(subjectRepository.findById(subjectId)).thenThrow(new EntityNotFoundException(javax.security.auth.Subject.class, "1"));

        assertThrows(EntityNotFoundException.class, () ->
                lecturerService.assignSubjectToLecturer(subjectId, lecturerId)
        );
    }

    @Test
    void removeSubjectToLecturer_removesSubjectFromSet_whenBothExist() {
        long subjectId = 10L;
        long lecturerId = 20L;

        Subject subject = new Subject();
        Lecturer lecturer = new Lecturer();
        Set<Subject> subjects = new HashSet<>();
        subjects.add(subject);
        lecturer.setSubjects(subjects);

        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));
        when(lecturerRepository.findById(lecturerId)).thenReturn(Optional.of(lecturer));

        lecturerService.removeSubjectFromLecturer(subjectId, lecturerId);

        assertTrue(lecturer.getSubjects().isEmpty());
        verify(subjectRepository).findById(subjectId);
        verify(lecturerRepository).findById(lecturerId);
    }

    @Test
    void removeSubjectFromLecturer_throwsServiceException_whenDaoExceptionOccurs() {
        long subjectId = 1L;
        long lecturerId = 2L;

        when(subjectRepository.findById(subjectId)).thenThrow(new ServiceException(""));

        assertThrows(ServiceException.class, () ->
                lecturerService.removeSubjectFromLecturer(subjectId, lecturerId)
        );
    }

    @Test
    void updateLecturer_whenValidLecturerDTO_thanLecturerIsUpdated() {
        Lecturer initial = getLecturer();
        LecturerDTO changes = getLecturerDto();
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

    LecturerDTO getLecturerDto() {
        LecturerDTO dto = new LecturerDTO();
        dto.setName("Jane");
        dto.setSureName("Doe");
        dto.setSalary(55000);
        return dto;
    }
}