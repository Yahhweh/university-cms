package placeholder.organisation.unicms.service.datagenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.entity.StudySubject;
import placeholder.organisation.unicms.service.LecturerService;
import placeholder.organisation.unicms.service.StudySubjectService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LecturerToStudySubjectGeneratorTest {

    @Mock
    private LecturerService lecturerService;
    @Mock
    private StudySubjectService subjectService;

    private LecturerToStudySubjectGenerator generator;

    private static final int LECTURERS_COUNT = 50;
    private static final int SUBJECTS_COUNT = 15;
    private static final int MIN_SUBJECTS = 1;
    private static final int MAX_SUBJECTS = 2;

    @BeforeEach
    void setUp() {
        generator = new LecturerToStudySubjectGenerator(
                lecturerService,
                subjectService,
                MIN_SUBJECTS,
                MAX_SUBJECTS
        );
    }

    @Test
    void generate_assignsExpectedRangeOfSubjects_whenDataIsValid() {
        List<Lecturer> lecturers = createLecturers(LECTURERS_COUNT);
        List<StudySubject> subjects = createSubjects(SUBJECTS_COUNT);

        when(lecturerService.findAllLecturers()).thenReturn(lecturers);
        when(subjectService.findAllSubjects()).thenReturn(subjects);

        generator.generate(LECTURERS_COUNT);

        verify(lecturerService, atLeast(LECTURERS_COUNT * MIN_SUBJECTS))
                .assignSubjectToLecturer(anyLong(), anyLong());

        verify(lecturerService, atMost(LECTURERS_COUNT * MAX_SUBJECTS))
                .assignSubjectToLecturer(anyLong(), anyLong());
    }

    @Test
    void generate_respectsAmountLimit_whenAmountIsSmallerThanLecturerList() {
        int totalInDb = 30;
        int requestAmount = 10;

        when(lecturerService.findAllLecturers()).thenReturn(createLecturers(totalInDb));
        when(subjectService.findAllSubjects()).thenReturn(createSubjects(SUBJECTS_COUNT));

        generator.generate(requestAmount);

        ArgumentCaptor<Long> lecturerIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(lecturerService, atLeastOnce())
                .assignSubjectToLecturer(lecturerIdCaptor.capture(), anyLong());

        long distinctLecturersProcessed = lecturerIdCaptor.getAllValues().stream()
                .distinct()
                .count();

        assertEquals(requestAmount, distinctLecturersProcessed);
    }

    @Test
    void generate_terminatesEarly_whenSubjectsTableIsEmpty() {
        when(lecturerService.findAllLecturers()).thenReturn(createLecturers(5));
        when(subjectService.findAllSubjects()).thenReturn(Collections.emptyList());

        generator.generate(10);

        verify(lecturerService, never()).assignSubjectToLecturer(anyLong(), anyLong());
    }

    @Test
    void generate_terminatesEarly_whenLecturersTableIsEmpty() {
        when(lecturerService.findAllLecturers()).thenReturn(Collections.emptyList());
        when(subjectService.findAllSubjects()).thenReturn(createSubjects(5));

        generator.generate(10);

        verify(lecturerService, never()).assignSubjectToLecturer(anyLong(), anyLong());
    }

    private List<Lecturer> createLecturers(int count) {
        return LongStream.rangeClosed(1, count)
                .mapToObj(i -> {
                    Lecturer l = new Lecturer();
                    l.setId(i);
                    l.setName("Lecturer" + i);
                    return l;
                })
                .collect(Collectors.toList());
    }

    private List<StudySubject> createSubjects(int count) {
        return LongStream.rangeClosed(1, count)
                .mapToObj(i -> {
                    StudySubject s = new StudySubject();
                    s.setId(i);
                    s.setName("Subject" + i);
                    return s;
                })
                .collect(Collectors.toList());
    }
}