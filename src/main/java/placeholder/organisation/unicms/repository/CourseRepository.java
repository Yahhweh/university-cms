package placeholder.organisation.unicms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import placeholder.organisation.unicms.entity.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {

    @Query("SELECT DISTINCT c FROM Course c " +
        "JOIN c.subjects s " +
        "WHERE s IN (SELECT subj FROM Lecturer l JOIN l.subjects subj " +
        "WHERE l.id = :lecturerId)")
    List<Course> findCoursesByLecturerId(@Param("lecturerId") Long lecturerId);
}