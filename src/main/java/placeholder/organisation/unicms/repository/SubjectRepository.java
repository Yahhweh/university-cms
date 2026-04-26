package placeholder.organisation.unicms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.entity.Subject;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long>, JpaSpecificationExecutor<Subject> {
    Optional<Subject> findByName(String name);

    @Query("SELECT DISTINCT s FROM Course c JOIN c.subjects s WHERE c.id = (SELECT g.course.id FROM Group g WHERE g.id = :groupId)")
    List<Subject> findSubjectsByGroupId(@Param("groupId") Long groupId);
}
