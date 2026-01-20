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
import java.util.Date;
import java.util.Optional;
import java.util.Set;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {
    @Mock
    LessonJpa lessonJpaMock;
    @Mock
    DurationJpa durationJpaMock;
    @Mock
    ClassRoomJpa classRoomJpaMock;
    @Mock
    GroupJpa groupJpaMock;
    @Mock
    StudySubjectJpa studySubjectJpaMock;
    @Mock
    LecturerJpa lecturerJpaMock;
    @InjectMocks
    LessonService lessonService;


    @Test
    void changeDuration_exists() {
        long durationId = 2;
        long lessonId = 1;
        Duration newDuration = new Duration(2L, LocalTime.now().plusHours(1), LocalTime.now().plusHours(1));
        Lesson lesson = getLesson();

        when(durationJpaMock.findById(durationId)).thenReturn(Optional.of(newDuration));
        when(lessonJpaMock.findById(lessonId)).thenReturn(Optional.ofNullable(lesson));

        assertThat(lesson.getDuration()).isEqualTo(getOldDuration());

        lessonService.changeDuration(lessonId, durationId);

        verify(durationJpaMock).findById(durationId);
        verify(lessonJpaMock).findById(lessonId);


        assertThat(lesson.getDuration()).isEqualTo(newDuration);
    }

    @Test
    void changeClassroom() {
        long classRoomId = 2;
        long lessonId = 1;
        Lesson lesson = getLesson();
        ClassRoom newRoom = new ClassRoom(2L, "311", new ClassRoomType(1L, "same", 50L));

        when(classRoomJpaMock.findById(classRoomId)).thenReturn(Optional.of(newRoom));
        when(lessonJpaMock.findById(lessonId)).thenReturn(Optional.of(lesson));

        assertThat(lesson.getClassRoom()).isEqualTo(getOldClassRoom());

        lessonService.changeClassroom(lessonId, classRoomId);

        verify(lessonJpaMock).findById(lessonId);
        verify(classRoomJpaMock).findById(classRoomId);
        assertThat(lesson.getClassRoom()).isEqualTo(newRoom);
    }

    @Test
    void changeGroup() {
        long groupId = 2L;
        long lessonId = 1L;
        Lesson lesson = getLesson();
        Group newGroup = new Group(2L, "12A");

        when(groupJpaMock.findById(groupId)).thenReturn(Optional.of(newGroup));
        when(lessonJpaMock.findById(lessonId)).thenReturn(Optional.ofNullable(lesson));

        assertThat(lesson.getGroup()).isEqualTo(getOldGroup());

        lessonService.changeGroup(lessonId, groupId);

        verify(groupJpaMock).findById(groupId);
        verify(lessonJpaMock).findById(lessonId);

        assertThat(lesson.getGroup()).isEqualTo(newGroup);
    }

    @Test
    void changeStudySubject() {
        long subjectId = 2L;
        long lessonId = 1L;
        Lesson lesson = getLesson();
        StudySubject newSubject = new StudySubject(subjectId, "Mathematics");

        when(studySubjectJpaMock.findById(subjectId)).thenReturn(Optional.of(newSubject));
        when(lessonJpaMock.findById(lessonId)).thenReturn(Optional.of(lesson));

        assertThat(lesson.getStudySubject()).isEqualTo(getOldSubject());

        lessonService.changeStudySubject(lessonId, subjectId);

        verify(lessonJpaMock).findById(lessonId);
        verify(studySubjectJpaMock).findById(subjectId);
        assertThat(lesson.getStudySubject()).isEqualTo(newSubject);
    }

    @Test
    void changeLecturer() {
        long lecturerId = 3L;
        long lessonId = 1L;
        Lesson lesson = getLesson();
        Lecturer newLecturer = new Lecturer((int) lecturerId, Set.of(getOldSubject()));

        when(lecturerJpaMock.findById(lecturerId)).thenReturn(Optional.of(newLecturer));
        when(lessonJpaMock.findById(lessonId)).thenReturn(Optional.of(lesson));

        assertThat(lesson.getLecturer()).isEqualTo(getLecturer());

        lessonService.changeLecturer(lessonId, lecturerId);

        verify(lessonJpaMock).findById(lessonId);
        verify(lecturerJpaMock).findById(lecturerId);
        assertThat(lesson.getLecturer()).isEqualTo(newLecturer);
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