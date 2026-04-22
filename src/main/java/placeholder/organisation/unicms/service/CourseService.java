package placeholder.organisation.unicms.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.Course;
import placeholder.organisation.unicms.repository.CourseRepository;
import placeholder.organisation.unicms.repository.SubjectRepository;
import placeholder.organisation.unicms.repository.specifications.CourseSpecification;
import placeholder.organisation.unicms.service.dto.request.CourseRequestDTO;
import placeholder.organisation.unicms.service.mapper.CourseMapper;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
@Log4j2
public class CourseService {

    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final CourseMapper courseMapper;

    public Page<Course> findAll(Pageable pageable, String name) {
        log.debug("Trying to get paginated Courses: {}", pageable);
        return courseRepository.findAll(CourseSpecification.filter(name), pageable);
    }

    public List<Course> findAllCourses() {
        List<Course> courses = courseRepository.findAll();
        log.debug("Found {} courses", courses.size());
        return courses;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void removeCourse(long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new EntityNotFoundException(Course.class, String.valueOf(courseId));
        }
        courseRepository.deleteById(courseId);
        log.debug("Deleted course with id: {}", courseId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void createCourse(CourseRequestDTO dto) {
        Course course = courseMapper.toEntity(dto);
        if (dto.getSubjectIds() != null && !dto.getSubjectIds().isEmpty()) {
            course.setSubjects(subjectRepository.findAllById(dto.getSubjectIds()));
        }
        courseRepository.save(course);
        log.info("Course saved successfully. Name: {}", course.getName());
    }

    public Optional<Course> findCourse(Long courseId) {
        log.debug("Trying to get course with id: {}", courseId);
        return courseRepository.findById(courseId);
    }

    public List<Course> findCoursesRelatedToLecturer(Long lecturerId){
        log.debug("Trying to get course with lecturerId: {}", lecturerId);
        return courseRepository.findCoursesByLecturerId(lecturerId);
    }
}