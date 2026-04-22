package placeholder.organisation.unicms.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.jdbc.Sql;
import placeholder.organisation.unicms.entity.Course;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest(
    includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CourseRepository.class)
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/datasets/courses_jpa.sql",
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class CourseRepositoryTest {

    @Autowired
    CourseRepository courseRepository;

    @Test
    void findCoursesByLecturerId_shouldReturnCourses_WhenLecturerHasMatchingSubjects() {
        Long targetLecturerId = 1L;
        int expectedCoursesSize = 1;

        List<Course> courses = courseRepository.findCoursesByLecturerId(targetLecturerId);

        assertThat(courses).isNotNull();
        assertThat(courses.size()).isEqualTo(expectedCoursesSize);

        Course foundCourse = courses.get(0);
        assertThat(foundCourse.getName()).isEqualTo("Programming");
    }

    @Test
    void findCoursesByLecturerId_shouldReturnEmpty_WhenLecturerDoesNotExist() {
        Long nonExistentLecturerId = 999L;

        List<Course> courses = courseRepository.findCoursesByLecturerId(nonExistentLecturerId);

        assertThat(courses.isEmpty()).isTrue();
    }
}
