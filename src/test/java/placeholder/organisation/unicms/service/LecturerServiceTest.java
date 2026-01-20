package placeholder.organisation.unicms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.dao.DaoException;
import placeholder.organisation.unicms.dao.LecturerJpa;
import placeholder.organisation.unicms.dao.StudySubjectJpa;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.entity.StudySubject;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LecturerServiceTest {

    @Mock
    private LecturerJpa lecturerJpa;

    @Mock
    private StudySubjectJpa studySubjectJpa;

    @InjectMocks
    private LecturerService lecturerService;

    @Test
    void assignSubjectToLecturer_callsRepositorySave_whenSubjectAndLecturerExist() {
        long subjectId = 1L;
        long lecturerId = 2L;

        StudySubject subject = new StudySubject();
        Lecturer lecturer = new Lecturer();
        lecturer.setStudySubjects(new HashSet<>());

        when(studySubjectJpa.findById(subjectId)).thenReturn(Optional.of(subject));
        when(lecturerJpa.findById(lecturerId)).thenReturn(Optional.of(lecturer));

        lecturerService.assignSubjectToLecturer(subjectId, lecturerId);

        assertTrue(lecturer.getStudySubjects().contains(subject));
        verify(studySubjectJpa).findById(subjectId);
        verify(lecturerJpa).findById(lecturerId);
    }

    @Test
    void assignSubjectToLecturer_throwsServiceException_whenDaoExceptionOccurs() {
        long subjectId = 1L;
        long lecturerId = 2L;

        when(studySubjectJpa.findById(subjectId)).thenThrow(new DaoException("Database error"));

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

        when(studySubjectJpa.findById(subjectId)).thenReturn(Optional.of(subject));
        when(lecturerJpa.findById(lecturerId)).thenReturn(Optional.of(lecturer));

        lecturerService.removeSubjectToLecturer(subjectId, lecturerId);

        assertTrue(lecturer.getStudySubjects().isEmpty());
        verify(studySubjectJpa).findById(subjectId);
        verify(lecturerJpa).findById(lecturerId);
    }

    @Test
    void removeSubjectToLecturer_throwsServiceException_whenDaoExceptionOccurs() {
        long subjectId = 1L;
        long lecturerId = 2L;

        when(studySubjectJpa.findById(subjectId)).thenThrow(new DaoException(""));

        assertThrows(ServiceException.class, () ->
                lecturerService.removeSubjectToLecturer(subjectId, lecturerId)
        );
    }
}