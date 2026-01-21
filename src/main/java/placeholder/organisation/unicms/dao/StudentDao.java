package placeholder.organisation.unicms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.Student;

@Repository
public interface StudentDao extends JpaRepository<Student, Long> {
}
