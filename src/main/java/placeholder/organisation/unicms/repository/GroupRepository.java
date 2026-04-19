package placeholder.organisation.unicms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.Course;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.entity.Subject;

import java.util.List;
import java.util.Set;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {

    List<Group> findByCourse(Course course);

    @Query("SELECT DISTINCT g FROM Group g JOIN g.course c JOIN c.subjects s WHERE s IN :subjects")
    List<Group> findDistinctByCourseSubjectsIn(@Param("subjects") Set<Subject> subjects);
}
