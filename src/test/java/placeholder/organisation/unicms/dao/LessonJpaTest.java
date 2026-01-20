package placeholder.organisation.unicms.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.jdbc.Sql;
import placeholder.organisation.unicms.entity.Lesson;
import placeholder.organisation.unicms.entity.PersonType;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest(
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = LessonJpa.class)
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/datasets/lesson_jpa.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class LessonJpaTest {

    @Autowired
    LessonJpa lessonJpa;

    @Test
    void findByLecturerId_shouldReturnLecturer_WhenLecturerExists() {
        Long targetLecturerId = 2L;
        int expectedLessonsSize = 3;

        List<Lesson> lessons = lessonJpa.findByLecturerId(targetLecturerId);

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
    void findByDateAndRole_ShouldReturnLessonByStudent_WhenUserIdProvided(){
        LocalDate lessonDate = LocalDate.parse("2026-01-17");
        long studentId = 1L;
        long groupId = 1L;
        int expectedLessonsSize = 1;

        List<Lesson> lessons = lessonJpa.findByDateAndRole(lessonDate,studentId, PersonType.Student);

        assertThat(lessons.size()).isEqualTo(expectedLessonsSize);
        Lesson foundLesson = lessons.get(0);

        assertThat(foundLesson.getGroup().getId()).isEqualTo(groupId);
        assertThat(foundLesson.getStudySubject().getName()).isEqualTo("Java Programming");
    }

    @Test
    void findByDateAndRole_ShouldReturnTrue_WhenUserDoesNotExists(){
        LocalDate lessonDate = LocalDate.parse("2026-01-17");
        long studentId = 1000L;

        List<Lesson> lessons = lessonJpa.findByDateAndRole(lessonDate,studentId, PersonType.Student);

        assertThat(lessons.isEmpty()).isTrue();
    }

    @Test
    void findByDateAndRole_ShouldReturnTrue_WhenDateDoesNotExists(){
        LocalDate lessonDate = LocalDate.parse("2026-02-17");
        long studentId = 1L;

        List<Lesson> lessons = lessonJpa.findByDateAndRole(lessonDate,studentId, PersonType.Student);

        assertThat(lessons.isEmpty()).isTrue();
    }

    @Test
    void findByDateAndRole_ShouldReturnLessonByLecturer_WhenLecturerIdProvided(){
        LocalDate lessonDate = LocalDate.parse("2026-01-17");
        long lecturerId = 2L;
        int expectedLessonsSize = 2;

        List<Lesson> lessons = lessonJpa.findByDateAndRole(lessonDate,lecturerId, PersonType.Lecturer);

        assertThat(lessons.size()).isEqualTo(expectedLessonsSize);
        Lesson foundLesson = lessons.get(0);

        assertThat(foundLesson.getLecturer()).isNotNull();
        assertThat(foundLesson.getLecturer().getName()).isEqualTo("Darius");
        assertThat(foundLesson.getStudySubject().getName()).isEqualTo("Java Programming");
    }

    @Test
    void findByDateAndRole_ShouldReturnTrueByLecturer_WhenLecturerDoesNotExists(){
        LocalDate lessonDate = LocalDate.parse("2026-01-17");
        long lecturerId = 200L;

        List<Lesson> lessons = lessonJpa.findByDateAndRole(lessonDate,lecturerId, PersonType.Lecturer);

        assertThat(lessons.isEmpty()).isTrue();
    }

    @Test
    void findInRange_shouldReturnTwoLessonsWithDifferentDateByStudent_WhenLessonsExists(){
        long studentId = 1L;
        int expectedLessonsSize = 2;

        String nameOfFirstLesson = "Java Programming";
        String nameOfSecondLesson = "Information Technologies";

        LocalDate fromDate = LocalDate.parse("2026-01-17");
        LocalDate toDate = LocalDate.parse("2026-01-18");

        List<Lesson> lessons = lessonJpa.findInRange(fromDate, toDate, studentId, PersonType.Student);


        assertThat(lessons.size()).isEqualTo(expectedLessonsSize);
        assertThat(lessons.get(0).getStudySubject().getName()).isEqualTo(nameOfFirstLesson);
        assertThat(lessons.get(1).getStudySubject().getName()).isEqualTo(nameOfSecondLesson);
    }

    @Test
    void findInRange_shouldReturnTwoLessonsWithDifferentDateByLecturer_WhenLessonsExists(){
        long lecturerId = 2L;
        int expectedLessonsSize = 3;

        String nameOfFirstLesson = "Java Programming";
        String nameOfSecondLesson = "Math";
        String nameOfThirdLesson = "Information Technologies";

        LocalDate fromDate = LocalDate.parse("2026-01-17");
        LocalDate toDate = LocalDate.parse("2026-01-18");

        List<Lesson> lessons = lessonJpa.findInRange(fromDate, toDate, lecturerId, PersonType.Lecturer);

        assertThat(lessons.size()).isEqualTo(expectedLessonsSize);
        assertThat(lessons.get(0).getStudySubject().getName()).isEqualTo(nameOfFirstLesson);
        assertThat(lessons.get(1).getStudySubject().getName()).isEqualTo(nameOfSecondLesson);
        assertThat(lessons.get(2).getStudySubject().getName()).isEqualTo(nameOfThirdLesson);
    }
}