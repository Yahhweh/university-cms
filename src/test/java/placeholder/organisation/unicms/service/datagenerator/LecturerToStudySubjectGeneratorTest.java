package placeholder.organisation.unicms.service.datagenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.entity.StudySubject;
import placeholder.organisation.unicms.service.LecturerService;
import placeholder.organisation.unicms.service.StudySubjectService;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LecturerToStudySubjectGeneratorTest {

    @Mock
    private LecturerService lecturerService;
    @Mock
    private StudySubjectService subjectService;

    private LecturerToStudySubjectGenerator generator;

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
        int count = 50;
        List<Lecturer> lecturers = createLecturers(count);
        List<StudySubject> subjects = createSubjects(15);

        when(lecturerService.findAllLecturers()).thenReturn(lecturers);
        when(subjectService.findAllSubjects()).thenReturn(subjects);

        generator.generate(count);

        verify(lecturerService, atLeast(count * MIN_SUBJECTS))
                .assignSubjectToLecturer(anyLong(), anyLong());

        verify(lecturerService, atMost(count * MAX_SUBJECTS))
                .assignSubjectToLecturer(anyLong(), anyLong());
    }

    @Test
    void generate_respectsAmountLimit_whenAmountIsSmallerThanLecturerList() {
        int totalInDb = 30;
        int requestAmount = 10;
        Set<Long> processedLecturerIds = new HashSet<>();

        when(lecturerService.findAllLecturers()).thenReturn(createLecturers(totalInDb));
        when(subjectService.findAllSubjects()).thenReturn(createSubjects(15));

        doAnswer(invocation -> {
            processedLecturerIds.add(invocation.getArgument(0));
            return null;
        }).when(lecturerService).assignSubjectToLecturer(anyLong(), anyLong());

        generator.generate(requestAmount);

        assertThat(processedLecturerIds).hasSize(requestAmount);
        assertThat(processedLecturerIds).allMatch(id -> id <= requestAmount);
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
                    return l;
                }).collect(Collectors.toList());
    }

    private List<StudySubject> createSubjects(int count) {
        return LongStream.rangeClosed(1, count)
                .mapToObj(i -> {
                    StudySubject s = new StudySubject();
                    s.setId(i);
                    return s;
                }).collect(Collectors.toList());
    }
}