package placeholder.organisation.unicms.service.datagenerator;


import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;

import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import placeholder.organisation.unicms.entity.StudySubject;

import placeholder.organisation.unicms.service.StudySubjectService;


import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class StudySubjectGeneratorTest {

    @Mock
    StudySubjectService studySubjectServiceMock;
    @InjectMocks
    StudySubjectGenerator studySubjectGenerator;

    @Test
    void generate_CreatesSpecifiedNumberOfSubjects_WhenAmountIsPositive() {
        int amount = 10;
        studySubjectGenerator.generate(amount);
        verify(studySubjectServiceMock, times(10))
                .createStudySubject(any(StudySubject.class));
    }

    @Test
    void generate_DoesNotCreateAnySubject_WhenAmountIsZero() {
        int amount = 0;
        studySubjectGenerator.generate(amount);
        verify(studySubjectServiceMock, times(0))
                .createStudySubject(any(StudySubject.class));
    }

    @Test
    void generate_ThrowsException_WhenAmountExceedsLimit() {
        int amount = 11;
        assertThrows(IndexOutOfBoundsException.class, () -> {
            studySubjectGenerator.generate(amount);
            verify(studySubjectServiceMock, times(10))
                    .createStudySubject(any(StudySubject.class));
        });
    }
}