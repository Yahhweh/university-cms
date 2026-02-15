package placeholder.organisation.unicms.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.jdbc.Sql;
import placeholder.organisation.unicms.entity.Lesson;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;


@DataJpaTest(
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = LessonRepository.class)
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/datasets/lesson_jpa.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class LessonRepositoryTest {
    private static final LocalDate TEST_DATE = LocalDate.of(2025, 10, 29);

    @Autowired
    LessonRepository lessonRepository;

    @Test
    void findByLecturerId_shouldReturnLecturer_WhenLecturerExists() {
        Long targetLecturerId = 2L;
        int expectedLessonsSize = 3;

        List<Lesson> lessons = lessonRepository.findByLecturerId(targetLecturerId);

        assertThat(lessons).isNotNull();

        assertThat(lessons.size()).isEqualTo(expectedLessonsSize);
        Lesson foundLesson = lessons.get(0);

        assertThat(foundLesson.getLecturer()).isNotNull();
        assertThat(foundLesson.getLecturer().getId()).isEqualTo(targetLecturerId);
        assertThat(foundLesson.getLecturer().getName()).isEqualTo("Darius");
        assertThat(foundLesson.getLecturer().getSalary().intValue()).isEqualTo(3000);

        assertThat(foundLesson.getStudySubject().getName()).isEqualTo("Java Programming");
    }

    @Test
    void findByDateAndRole_ShouldReturnLessonByStudent_WhenUserIdProvided() {
        LocalDate lessonDate = LocalDate.parse("2026-01-17");
        long studentId = 1L;
        long groupId = 1L;
        int expectedLessonsSize = 1;

        List<Lesson> lessons = lessonRepository.findByDateForStudent(lessonDate, studentId);

        assertThat(lessons.size()).isEqualTo(expectedLessonsSize);
        Lesson foundLesson = lessons.get(0);

        assertThat(foundLesson.getGroup().getId()).isEqualTo(groupId);
        assertThat(foundLesson.getStudySubject().getName()).isEqualTo("Java Programming");
    }

    @Test
    void findByDateAndRole_ShouldReturnTrue_WhenUserDoesNotExists() {
        LocalDate lessonDate = LocalDate.parse("2026-01-17");
        long studentId = 1000L;

        List<Lesson> lessons = lessonRepository.findByDateForStudent(lessonDate, studentId);

        assertThat(lessons.isEmpty()).isTrue();
    }

    @Test
    void findByDateAndRole_ShouldReturnTrue_WhenDateDoesNotExists() {
        LocalDate lessonDate = LocalDate.parse("2026-02-17");
        long studentId = 1L;

        List<Lesson> lessons = lessonRepository.findByDateForStudent(lessonDate, studentId);

        assertThat(lessons.isEmpty()).isTrue();
    }

    @Test
    void findByDateAndRole_ShouldReturnLessonByLecturer_WhenLecturerIdProvided() {
        LocalDate lessonDate = LocalDate.parse("2026-01-17");
        long lecturerId = 2L;
        int expectedLessonsSize = 2;

        List<Lesson> lessons = lessonRepository.findByDateForLecturer(lessonDate, lecturerId);

        assertThat(lessons.size()).isEqualTo(expectedLessonsSize);
        Lesson foundLesson = lessons.get(0);

        assertThat(foundLesson.getLecturer()).isNotNull();
        assertThat(foundLesson.getLecturer().getName()).isEqualTo("Darius");
        assertThat(foundLesson.getStudySubject().getName()).isEqualTo("Java Programming");
    }

    @Test
    void findByDateAndRole_ShouldReturnTrueByLecturer_WhenLecturerDoesNotExists() {
        LocalDate lessonDate = LocalDate.parse("2026-01-17");
        long lecturerId = 200L;

        List<Lesson> lessons = lessonRepository.findByDateForLecturer(lessonDate, lecturerId);

        assertThat(lessons.isEmpty()).isTrue();
    }

    @Test
    void findInRange_shouldReturnTwoLessonsWithDifferentDateByStudent_WhenLessonsExists() {
        long studentId = 1L;
        int expectedLessonsSize = 2;

        String nameOfFirstLesson = "Java Programming";
        String nameOfSecondLesson = "Information Technologies";

        LocalDate fromDate = LocalDate.parse("2026-01-17");
        LocalDate toDate = LocalDate.parse("2026-01-18");

        List<Lesson> lessons = lessonRepository.findInRangeForStudent(fromDate, toDate, studentId);


        assertThat(lessons.size()).isEqualTo(expectedLessonsSize);
        assertThat(lessons.get(0).getStudySubject().getName()).isEqualTo(nameOfFirstLesson);
        assertThat(lessons.get(1).getStudySubject().getName()).isEqualTo(nameOfSecondLesson);
    }

    @Test
    void findInRange_shouldReturnTwoLessonsWithDifferentDateByLecturer_WhenLessonsExists() {
        long lecturerId = 2L;
        int expectedLessonsSize = 3;

        String nameOfFirstLesson = "Java Programming";
        String nameOfSecondLesson = "Math";
        String nameOfThirdLesson = "Information Technologies";

        LocalDate fromDate = LocalDate.parse("2026-01-17");
        LocalDate toDate = LocalDate.parse("2026-01-18");

        List<Lesson> lessons = lessonRepository.findInRangeForLecturer(fromDate, toDate, lecturerId);

        assertThat(lessons.size()).isEqualTo(expectedLessonsSize);
        assertThat(lessons.get(0).getStudySubject().getName()).isEqualTo(nameOfFirstLesson);
        assertThat(lessons.get(1).getStudySubject().getName()).isEqualTo(nameOfSecondLesson);
        assertThat(lessons.get(2).getStudySubject().getName()).isEqualTo(nameOfThirdLesson);
    }

    @Test
    void isClassRoomFree_ReturnsConsistentResults() {
        LocalTime startTime = LocalTime.of(13, 00);
        LocalTime endTime = LocalTime.of(14, 30);
        long id = 1L;

        boolean isRoomFree = lessonRepository.findConflicts(TEST_DATE, startTime, endTime, id);

        assertFalse(isRoomFree);
    }
}