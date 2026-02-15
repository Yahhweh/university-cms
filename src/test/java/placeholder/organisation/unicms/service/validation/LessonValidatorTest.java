package placeholder.organisation.unicms.service.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.service.EntityValidationException;
import placeholder.organisation.unicms.repository.GroupRepository;
import placeholder.organisation.unicms.repository.LessonRepository;
import placeholder.organisation.unicms.repository.StudentRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LessonValidatorTest {

    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private LessonValidator lessonValidator;

    @Test
    void validateLesson_shouldThrowException_whenGroupHasConflict() {
        Lesson lesson = createBaseLesson();

        when(lessonRepository.findConflictionLessonsForLecturer(any(), any(), any(), any(), anyLong())).thenReturn(false);
        when(lessonRepository.findConflicts(any(), any(), any(), anyLong(), anyLong())).thenReturn(false);
        when(lessonRepository.existsGroupConflict(any(), any(), any(), any(), anyLong())).thenReturn(true);

        assertThatThrownBy(() -> lessonValidator.validateLesson(lesson))
                .isInstanceOf(EntityValidationException.class)
                .hasMessageContaining("Group has another lesson at this time");
    }

    @Test
    void validateLesson_shouldThrowException_whenLecturerNotAuthorizedForSubject() {
        Lesson lesson = createBaseLesson();
        lesson.getLecturer().setStudySubjects(new HashSet<>());

        mockNoConflicts();

        assertThatThrownBy(() -> lessonValidator.validateLesson(lesson))
                .isInstanceOf(EntityValidationException.class)
                .hasMessageContaining("Lecturer is not authorized");
    }

    @Test
    void validateLesson_shouldThrowException_whenCapacityIsNotSufficient() {
        Lesson lesson = createBaseLesson();
        long roomCapacity = lesson.getClassRoom().getClassRoomType().getCapacity();

        mockNoConflicts();
        lesson.getLecturer().getStudySubjects().add(lesson.getStudySubject());

        when(groupRepository.findById(anyLong())).thenReturn(Optional.of(lesson.getGroup()));

        List<Student> overCapacityList = Arrays.asList(new Student[Math.toIntExact(roomCapacity + 1)]);
        when(studentRepository.findStudentsByGroup(any(Group.class))).thenReturn(overCapacityList);

        assertThatThrownBy(() -> lessonValidator.validateLesson(lesson))
                .isInstanceOf(EntityValidationException.class)
                .hasMessageContaining("Group size exceeds room capacity");
    }

    @Test
    void validateLesson_shouldPass_whenAllConditionsAreMet() {
        Lesson lesson = createBaseLesson();
        lesson.getLecturer().getStudySubjects().add(lesson.getStudySubject());

        mockNoConflicts();
        when(groupRepository.findById(anyLong())).thenReturn(Optional.of(lesson.getGroup()));

        List<Student> normalList = Arrays.asList(new Student[50]);
        when(studentRepository.findStudentsByGroup(any(Group.class))).thenReturn(normalList);

        assertDoesNotThrow(() -> lessonValidator.validateLesson(lesson));
    }

    private void mockNoConflicts() {
        when(lessonRepository.findConflictionLessonsForLecturer(any(), any(), any(), any(), anyLong())).thenReturn(false);
        when(lessonRepository.findConflicts(any(), any(), any(), anyLong(), anyLong())).thenReturn(false);
        when(lessonRepository.existsGroupConflict(any(), any(), any(), any(), anyLong())).thenReturn(false);
    }

    private Lesson createBaseLesson() {
        StudySubject subject = new StudySubject(1L, "Math");
        Lecturer lecturer = getLecturer();
        lecturer.setStudySubjects(new HashSet<>());

        return new Lesson(
                10L,
                new Duration(1L, LocalTime.of(9, 0), LocalTime.of(10, 30)),
                subject,
                getGroup(),
                lecturer,
                getClassRoom(),
                LocalDate.now()
        );
    }

    private Lecturer getLecturer() {
        Lecturer lecturer = new Lecturer();
        lecturer.setId(1L);
        lecturer.setName("John");
        lecturer.setSureName("Doe");
        return lecturer;
    }

    private Group getGroup() {
        return new Group(1L, "A-122");
    }

    private ClassRoom getClassRoom() {
        return new ClassRoom(1L, "A-101", new ClassRoomType(1L, "Hall", 100L));
    }
}