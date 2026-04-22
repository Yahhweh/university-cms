package placeholder.organisation.unicms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.Course;
import placeholder.organisation.unicms.repository.CourseRepository;
import placeholder.organisation.unicms.repository.SubjectRepository;
import placeholder.organisation.unicms.service.mapper.CourseMapper;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Spy
    CourseMapper courseMapper = Mappers.getMapper(CourseMapper.class);

    @InjectMocks
    private CourseService courseService;

    @Test
    void findCoursesRelatedToLecturer_shouldReturnCourses_WhenLecturerExists() {
        long lecturerId = 1L;
        List<Course> expected = List.of(getCourse());

        when(courseRepository.findCoursesByLecturerId(lecturerId)).thenReturn(expected);

        List<Course> result = courseService.findCoursesRelatedToLecturer(lecturerId);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(expected.size());
        verify(courseRepository).findCoursesByLecturerId(lecturerId);
    }

    private Course getCourse() {
        Course course = new Course();
        course.setId(1L);
        course.setName("Programming");
        return course;
    }
}
