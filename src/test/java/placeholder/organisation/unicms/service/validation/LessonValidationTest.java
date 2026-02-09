package placeholder.organisation.unicms.service.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.excpetion.EntityValidationException;
import placeholder.organisation.unicms.repository.ClassRoomRepository;
import placeholder.organisation.unicms.repository.StudentRepository;
import placeholder.organisation.unicms.repository.LessonRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LessonValidationTest {
    @Mock
    LessonRepository lessonRepository;
    @Mock
    StudentRepository studentRepository;
    @Mock
    ClassRoomRepository classRoomRepository;


    @InjectMocks
    LessonValidation lessonValidation;

    @Test
    void isLecturerDoesntHasLecturesInTheSameTime_shouldThrowException_whenNewLessonOverlapsWithExistingLesson() {
        Lesson existingLesson = new Lesson(
                1L,
                new Duration(1L, LocalTime.of(8, 30), LocalTime.of(10, 00)),
                new StudySubject(1L, "Math"),
                getGroup(),
                getLecturer(),
                getClassRoom(),
                LocalDate.now()
        );

        Lesson newLesson = new Lesson(
                null,
                new Duration(2L, LocalTime.of(9, 00), LocalTime.of(10, 20)),
                new StudySubject(2L, "PE"),
                new Group(2L, "B-121"),
                getLecturer(),
                getSecondClassRoom(),
                LocalDate.now()
        );

        when(lessonRepository.findAllLessonsRelatedToLecturer(getLecturer().getId()))
                .thenReturn(List.of(existingLesson));


        assertThatThrownBy(() -> lessonValidation.validateLesson(newLesson))
                .isInstanceOf(EntityValidationException.class);
    }
    @Test
    void isLecturerDoesntHasLecturesInTheSameTime_shouldNotThrowException_whenNewLessonIsOnDifferentDate() {
        Lesson existingLesson = new Lesson(
                1L,
                new Duration(1L, LocalTime.of(8, 30), LocalTime.of(10, 00)),
                new StudySubject(1L, "Math"),
                getGroup(),
                getLecturer(),
                getClassRoom(),
                LocalDate.now()
        );

        Lesson newLesson = new Lesson(
                null,
                new Duration(1L, LocalTime.of(8, 30), LocalTime.of(10, 00)),
                new StudySubject(2L, "PE"),
                getGroup(),
                getLecturer(),
                getSecondClassRoom(),
                LocalDate.now().plusDays(1)
        );

        when(lessonRepository.findAllLessonsRelatedToLecturer(getLecturer().getId()))
                .thenReturn(List.of(existingLesson));

        when(classRoomRepository.findFreeClassRooms(
                newLesson.getDate(),
                newLesson.getDuration().getStart(),
                newLesson.getDuration().getEnd()))
                .thenReturn(List.of(getSecondClassRoom()));

        when(studentRepository.findStudentsByGroup(newLesson.getGroup()))
                .thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> lessonValidation.validateLesson(newLesson));
    }

    @Test
    void isLecturerDoesntHasLecturesInTheSameTime_shouldNotThrowException_whenLessonsAreBackToBack() {
        Lesson existingLesson = new Lesson(
                1L,
                new Duration(1L, LocalTime.of(8, 30), LocalTime.of(10, 00)),
                new StudySubject(1L, "Math"),
                getGroup(),
                getLecturer(),
                getClassRoom(),
                LocalDate.now()
        );

        Lesson newLesson = new Lesson(
                null,
                new Duration(2L, LocalTime.of(10, 00), LocalTime.of(11, 30)),
                new StudySubject(2L, "PE"),
                getGroup(),
                getLecturer(),
                getSecondClassRoom(),
                LocalDate.now()
        );

        when(lessonRepository.findAllLessonsRelatedToLecturer(getLecturer().getId()))
                .thenReturn(List.of(existingLesson));

        when(classRoomRepository.findFreeClassRooms(
                newLesson.getDate(),
                newLesson.getDuration().getStart(),
                newLesson.getDuration().getEnd()))
                .thenReturn(List.of(getSecondClassRoom()));

        when(studentRepository.findStudentsByGroup(newLesson.getGroup()))
                .thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> lessonValidation.validateLesson(newLesson));
    }

    @Test
    void isLecturerDoesntHasLecturesInTheSameTime_shouldNotThrowException_whenNoExistingLessons() {
        Lesson newLesson = new Lesson(
                null,
                new Duration(1L, LocalTime.of(8, 30), LocalTime.of(10, 00)),
                new StudySubject(1L, "Math"),
                getGroup(),
                getLecturer(),
                getClassRoom(),
                LocalDate.now()
        );

        when(lessonRepository.findAllLessonsRelatedToLecturer(getLecturer().getId()))
                .thenReturn(List.of());

        when(classRoomRepository.findFreeClassRooms(
                newLesson.getDate(),
                newLesson.getDuration().getStart(),
                newLesson.getDuration().getEnd()))
                .thenReturn(List.of(getClassRoom()));

        when(studentRepository.findStudentsByGroup(newLesson.getGroup()))
                .thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> lessonValidation.validateLesson(newLesson));
    }


    @Test
    void isGroupSizeSmallerThenClassRoomTypeCapacity_shouldThrowException_whenRoomCapacityIsLess(){
        ClassRoom classRoom = getClassRoom();
        ClassRoomType classRoomType = classRoom.getClassRoomType();
        classRoomType.setCapacity(10L);
        Group group = getGroup();
        Lesson newLesson = new Lesson(
                null,
                new Duration(1L, LocalTime.of(8, 30), LocalTime.of(10, 00)),
                new StudySubject(1L, "Math"),
                group,
                getLecturer(),
                classRoom,
                LocalDate.now());

        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            students.add(new Student());
        }

        when(studentRepository.findStudentsByGroup(group)).thenReturn(students);

        when(classRoomRepository.findFreeClassRooms(
                newLesson.getDate(),
                newLesson.getDuration().getStart(),
                newLesson.getDuration().getEnd()))
                .thenReturn(List.of(classRoom));

        when(lessonRepository.findAllLessonsRelatedToLecturer(getLecturer().getId()))
                .thenReturn(List.of());

        assertThatThrownBy(() -> lessonValidation.validateLesson(newLesson))
                .isInstanceOf(EntityValidationException.class);
    }

    @Test
    void isGroupSizeSmallerThenClassRoomTypeCapacity_shouldNOTThrowException_whenRoomCapacityIsMore(){
        ClassRoom classRoom = getClassRoom();
        ClassRoomType classRoomType = classRoom.getClassRoomType();
        classRoomType.setCapacity(10L);
        Group group = getGroup();
        Lesson newLesson = new Lesson(
                null,
                new Duration(1L, LocalTime.of(8, 30), LocalTime.of(10, 00)),
                new StudySubject(1L, "Math"),
                group,
                getLecturer(),
                classRoom,
                LocalDate.now());

        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            students.add(new Student());
        }

        when(studentRepository.findStudentsByGroup(group)).thenReturn(students);

        when(classRoomRepository.findFreeClassRooms(
                newLesson.getDate(),
                newLesson.getDuration().getStart(),
                newLesson.getDuration().getEnd()))
                .thenReturn(List.of(classRoom));

        when(lessonRepository.findAllLessonsRelatedToLecturer(getLecturer().getId()))
                .thenReturn(List.of());

        assertDoesNotThrow(() -> lessonValidation.validateLesson(newLesson));
    }

    @Test
    void isRoomIsFree_shouldThrowException_whenClassRoomIsBooked(){
        ClassRoom classRoom = getClassRoom();
        ClassRoom anotherClassRoom = getSecondClassRoom();
        Group group = getGroup();

        Lesson newLesson = new Lesson(
                null,
                new Duration(1L, LocalTime.of(8, 30), LocalTime.of(10, 00)),
                new StudySubject(1L, "Math"),
                group,
                getLecturer(),
                classRoom,
                LocalDate.now());

        List<ClassRoom> freeClassRooms = List.of(anotherClassRoom);

        when(classRoomRepository.findFreeClassRooms(
                newLesson.getDate(),
                newLesson.getDuration().getStart(),
                newLesson.getDuration().getEnd()))
                .thenReturn(freeClassRooms);

        when(lessonRepository.findAllLessonsRelatedToLecturer(getLecturer().getId()))
                .thenReturn(List.of());

        assertThatThrownBy(() -> lessonValidation.validateLesson(newLesson))
                .isInstanceOf(EntityValidationException.class);
    }

    @Test
    void isRoomIsFree_shouldNotThrowException_whenClassRoomIsFree(){
        ClassRoom classRoom = getClassRoom();
        Group group = getGroup();

        Lesson newLesson = new Lesson(
                null,
                new Duration(1L, LocalTime.of(8, 30), LocalTime.of(10, 00)),
                new StudySubject(1L, "Math"),
                group,
                getLecturer(),
                classRoom,
                LocalDate.now());

        List<ClassRoom> freeClassRooms = List.of(classRoom, getSecondClassRoom());

        when(classRoomRepository.findFreeClassRooms(
                newLesson.getDate(),
                newLesson.getDuration().getStart(),
                newLesson.getDuration().getEnd()))
                .thenReturn(freeClassRooms);

        when(studentRepository.findStudentsByGroup(group))
                .thenReturn(new ArrayList<>());

        when(lessonRepository.findAllLessonsRelatedToLecturer(getLecturer().getId()))
                .thenReturn(List.of());

        assertDoesNotThrow(() -> lessonValidation.validateLesson(newLesson));
    }

    Lecturer getLecturer() {
        Lecturer lecturer = new Lecturer();
        lecturer.setId(1L);
        lecturer.setName("John");
        lecturer.setSureName("Doe");
        lecturer.setSalary(40000);
        return lecturer;
    }

    Group getGroup() {
        return new Group(1L, "A-122");
    }

    ClassRoom getClassRoom() {
        return new ClassRoom(1L, "A-101", new ClassRoomType(1L, "Hall", 100L));
    }

    ClassRoom getSecondClassRoom() {
        return new ClassRoom(2L, "B-101", new ClassRoomType(2L, "Laboratory", 100L));
    }
}