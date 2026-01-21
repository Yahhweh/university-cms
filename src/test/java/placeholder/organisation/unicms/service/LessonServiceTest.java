package placeholder.organisation.unicms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.dao.*;
import placeholder.organisation.unicms.entity.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {
    @Mock
    LessonDao lessonDaoMock;
    @Mock
    DurationDao durationDaoMock;
    @Mock
    ClassRoomDao classRoomDaoMock;
    @Mock
    GroupDao groupDaoMock;
    @Mock
    StudySubjectDao studySubjectDaoMock;
    @Mock
    LecturerDao lecturerDaoMock;
    @InjectMocks
    LessonService lessonService;


    @Test
    void changeDuration_exists() {
        long durationId = 2;
        long lessonId = 1;
        Duration newDuration = new Duration(2L, LocalTime.now().plusHours(1), LocalTime.now().plusHours(1));
        Lesson lesson = getLesson();

        when(durationDaoMock.findById(durationId)).thenReturn(Optional.of(newDuration));
        when(lessonDaoMock.findById(lessonId)).thenReturn(Optional.ofNullable(lesson));

        assertThat(lesson.getDuration()).isEqualTo(getOldDuration());

        lessonService.changeDuration(lessonId, durationId);

        verify(durationDaoMock).findById(durationId);
        verify(lessonDaoMock).findById(lessonId);


        assertThat(lesson.getDuration()).isEqualTo(newDuration);
    }

    @Test
    void changeClassroom() {
        long classRoomId = 2;
        long lessonId = 1;
        Lesson lesson = getLesson();
        ClassRoom newRoom = new ClassRoom(2L, "311", new ClassRoomType(1L, "same", 50L));

        when(classRoomDaoMock.findById(classRoomId)).thenReturn(Optional.of(newRoom));
        when(lessonDaoMock.findById(lessonId)).thenReturn(Optional.of(lesson));

        assertThat(lesson.getClassRoom()).isEqualTo(getOldClassRoom());

        lessonService.changeClassroom(lessonId, classRoomId);

        verify(lessonDaoMock).findById(lessonId);
        verify(classRoomDaoMock).findById(classRoomId);
        assertThat(lesson.getClassRoom()).isEqualTo(newRoom);
    }

    @Test
    void changeGroup() {
        long groupId = 2L;
        long lessonId = 1L;
        Lesson lesson = getLesson();
        Group newGroup = new Group(2L, "12A");

        when(groupDaoMock.findById(groupId)).thenReturn(Optional.of(newGroup));
        when(lessonDaoMock.findById(lessonId)).thenReturn(Optional.ofNullable(lesson));

        assertThat(lesson.getGroup()).isEqualTo(getOldGroup());

        lessonService.changeGroup(lessonId, groupId);

        verify(groupDaoMock).findById(groupId);
        verify(lessonDaoMock).findById(lessonId);

        assertThat(lesson.getGroup()).isEqualTo(newGroup);
    }

    @Test
    void changeStudySubject() {
        long subjectId = 2L;
        long lessonId = 1L;
        Lesson lesson = getLesson();
        StudySubject newSubject = new StudySubject(subjectId, "Mathematics");

        when(studySubjectDaoMock.findById(subjectId)).thenReturn(Optional.of(newSubject));
        when(lessonDaoMock.findById(lessonId)).thenReturn(Optional.of(lesson));

        assertThat(lesson.getStudySubject()).isEqualTo(getOldSubject());

        lessonService.changeStudySubject(lessonId, subjectId);

        verify(lessonDaoMock).findById(lessonId);
        verify(studySubjectDaoMock).findById(subjectId);
        assertThat(lesson.getStudySubject()).isEqualTo(newSubject);
    }

    @Test
    void changeLecturer() {
        long lecturerId = 3L;
        long lessonId = 1L;
        Lesson lesson = getLesson();
        Lecturer newLecturer = new Lecturer((int) lecturerId, Set.of(getOldSubject()));

        when(lecturerDaoMock.findById(lecturerId)).thenReturn(Optional.of(newLecturer));
        when(lessonDaoMock.findById(lessonId)).thenReturn(Optional.of(lesson));

        assertThat(lesson.getLecturer()).isEqualTo(getLecturer());

        lessonService.changeLecturer(lessonId, lecturerId);

        verify(lessonDaoMock).findById(lessonId);
        verify(lecturerDaoMock).findById(lecturerId);
        assertThat(lesson.getLecturer()).isEqualTo(newLecturer);
    }

    @Test
    void findLessonsInRange_shouldReturnLessons_whenDataExists() {
        LocalDate start = LocalDate.of(2025, 10, 1);
        LocalDate end = LocalDate.of(2025, 10, 31);
        long personId = 1L;
        PersonType type = PersonType.Lecturer;
        Lesson lesson = getLesson();

        when(lessonDaoMock.findInRange(start, end, personId, type))
                .thenReturn(List.of(lesson));

        List<Lesson> result = lessonService.findLessonsInRange(start, end, personId, type);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(lesson);
        verify(lessonDaoMock).findInRange(start, end, personId, type);
    }

    @Test
    void findByDate_shouldReturnEmptyList_whenNoLessonsFound() {
        LocalDate date = LocalDate.of(2025, 12, 25);
        long personId = 1L;
        PersonType type = PersonType.Student;

        when(lessonDaoMock.findByDateAndRole(date, personId, type))
                .thenReturn(List.of());

        List<Lesson> result = lessonService.findByDate(date, personId, type);

        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isTrue();
        verify(lessonDaoMock).findByDateAndRole(date, personId, type);
    }

    private static Duration getOldDuration() {
        return new Duration(1L, LocalTime.of(12, 12), LocalTime.of(13, 13));
    }

    private static StudySubject getOldSubject() {
        return new StudySubject(1L, "Lesson");
    }

    private static Group getOldGroup() {
        return new Group(1L, "1s");
    }

    private static Lecturer getLecturer() {
        return new Lecturer(2, Set.of(getOldSubject()));
    }

    private static ClassRoom getOldClassRoom() {
        return new ClassRoom(1L, "214", new ClassRoomType(1L, "type", 50L));
    }

    Lesson getLesson() {
        StudySubject subject = getOldSubject();
        Duration duration = getOldDuration();
        Group group = getOldGroup();
        Lecturer lecturer = getLecturer();
        ClassRoom classRoom = getOldClassRoom();
        return new Lesson(1L, duration, subject, group, lecturer, classRoom, LocalDate.of(2025, 10, 12));
    }
}