package placeholder.organisation.unicms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.repository.RepositoryException;
import placeholder.organisation.unicms.repository.LecturerRepository;
import placeholder.organisation.unicms.repository.StudySubjectRepository;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.entity.StudySubject;
import placeholder.organisation.unicms.service.dto.LecturerDTO;
import placeholder.organisation.unicms.service.mapper.LecturerMapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LecturerServiceTest {

    @Mock
    private LecturerRepository lecturerRepository;

    @Mock
    private StudySubjectRepository studySubjectRepository;

    @Spy
    LecturerMapper lecturerMapper = Mappers.getMapper(LecturerMapper.class);

    @InjectMocks
    private LecturerService lecturerService;

    @Test
    void assignSubjectToLecturer_callsRepositorySave_whenSubjectAndLecturerExist() {
        long subjectId = 1L;
        long lecturerId = 2L;

        StudySubject subject = new StudySubject();
        Lecturer lecturer = new Lecturer();
        lecturer.setStudySubjects(new HashSet<>());

        when(studySubjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));
        when(lecturerRepository.findById(lecturerId)).thenReturn(Optional.of(lecturer));

        lecturerService.assignSubjectToLecturer(subjectId, lecturerId);

        assertTrue(lecturer.getStudySubjects().contains(subject));
        verify(studySubjectRepository).findById(subjectId);
        verify(lecturerRepository).findById(lecturerId);
    }

    @Test
    void assignSubjectToLecturer_throwsServiceException_whenDaoExceptionOccurs() {
        long subjectId = 1L;
        long lecturerId = 2L;

        when(studySubjectRepository.findById(subjectId)).thenThrow(new RepositoryException("Database error"));

        assertThrows(ServiceException.class, () ->
                lecturerService.assignSubjectToLecturer(subjectId, lecturerId)
        );
    }

    @Test
    void removeSubjectToLecturer_removesSubjectFromSet_whenBothExist() {
        long subjectId = 10L;
        long lecturerId = 20L;

        StudySubject subject = new StudySubject();
        Lecturer lecturer = new Lecturer();
        Set<StudySubject> subjects = new HashSet<>();
        subjects.add(subject);
        lecturer.setStudySubjects(subjects);

        when(studySubjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));
        when(lecturerRepository.findById(lecturerId)).thenReturn(Optional.of(lecturer));

        lecturerService.removeSubjectToLecturer(subjectId, lecturerId);

        assertTrue(lecturer.getStudySubjects().isEmpty());
        verify(studySubjectRepository).findById(subjectId);
        verify(lecturerRepository).findById(lecturerId);
    }

    @Test
    void removeSubjectToLecturer_throwsServiceException_whenDaoExceptionOccurs() {
        long subjectId = 1L;
        long lecturerId = 2L;

        when(studySubjectRepository.findById(subjectId)).thenThrow(new RepositoryException(""));

        assertThrows(ServiceException.class, () ->
                lecturerService.removeSubjectToLecturer(subjectId, lecturerId)
        );
    }

    @Test
    void updateLecturer_whenValidLecturerDTO_thenLecturerIsUpdated() {
        Lecturer initial = getLecturer();
        LecturerDTO changes = getLecturerDto();
        long id = initial.getId();

        when(lecturerRepository.findById(id)).thenReturn(Optional.of(initial));

        lecturerService.updateLecturer(id, changes);

        verify(lecturerMapper).updateEntityFromDto(changes, initial);
        verify(lecturerRepository).save(initial);

        assertThat(initial.getName()).isEqualTo("Ivan");
        assertThat(initial.getSalary()).isEqualTo(55000);
    }

    Lecturer getLecturer() {
        Lecturer lecturer = new Lecturer();
        lecturer.setId(1L);
        lecturer.setName("Petr");
        lecturer.setSureName("Petrov");
        lecturer.setSalary(40000);
        return lecturer;
    }

    LecturerDTO getLecturerDto() {
        LecturerDTO dto = new LecturerDTO();
        dto.setName("Ivan");
        dto.setSureName("Ivanov");
        dto.setSalary(55000);
        return dto;
    }
}