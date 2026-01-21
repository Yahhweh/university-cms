package placeholder.organisation.unicms.service.datagenerator;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Service;
import placeholder.organisation.unicms.service.StudentService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UniversityFacadeTest {
    private static final int SUBJECT_AMOUNT = 10;
    private static final int STUDENT_AMOUNT = 150;
    private static final int GROUPS_AMOUNT = 20;
    private static final int LECTURER_AMOUNT = 13;
    private static final int ADDRESS_AMOUNT = STUDENT_AMOUNT + LECTURER_AMOUNT;
    private static final int CLASSROOM_TYPE_AMOUNT = 15;
    private static final int CLASSROOM_AMOUNT = 50;
    private static final int DURATION_AMOUNT = 6;

    private static final int MIN_STUDENTS_IN_GROUP = 10;
    private static final int MAX_STUDENTS_IN_GROUP = 30;

    @Mock
    StudentService studentService;
    @Mock
    AddressGenerator addressGenerator;
    @Mock
    ClassRoomTypeGenerator classRoomTypeGenerator;
    @Mock
    ClassRoomGenerator classRoomGenerator;
    @Mock
    DurationGenerator durationGenerator;
    @Mock
    GroupGenerator groupGenerator;
    @Mock
    LecturerGenerator lecturerGenerator;
    @Mock
    LecturerToStudySubjectGenerator lecturerToStudySubjectGenerator;
    @Mock
    LessonGenerator lessonGenerator;
    @Mock
    StudentsGenerator studentsGenerator;
    @Mock
    StudySubjectGenerator studySubjectGenerator;
    @Mock
    DbCleaningService cleaningService;

    @InjectMocks
    UniversityFacade universityFacade;


    @Test
    void initializeDataBase() {
        when(studentService.findAllStudents()).thenReturn(Collections.emptyList());

        universityFacade.initializeDataBase();

        verify(studentsGenerator).generate(STUDENT_AMOUNT);
        verify(addressGenerator).generate(ADDRESS_AMOUNT);
        verify(classRoomTypeGenerator).generate(CLASSROOM_TYPE_AMOUNT);
        verify(classRoomGenerator).generate(CLASSROOM_AMOUNT);
        verify(durationGenerator).generate(DURATION_AMOUNT);
        verify(groupGenerator).generate(GROUPS_AMOUNT);
        verify(studySubjectGenerator).generate(SUBJECT_AMOUNT);
        verify(lecturerGenerator).generate(LECTURER_AMOUNT);
        verify(lecturerToStudySubjectGenerator).generate(LECTURER_AMOUNT);
        verify(studentsGenerator).generate(STUDENT_AMOUNT);
        verify(studentsGenerator).assignRandomGroups(MIN_STUDENTS_IN_GROUP, MAX_STUDENTS_IN_GROUP);
        verify(lessonGenerator).generate(any(Integer.class));
        verify(cleaningService).deleteAll();
    }
}