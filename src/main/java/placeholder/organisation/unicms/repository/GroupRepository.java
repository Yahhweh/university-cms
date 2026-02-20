package placeholder.organisation.unicms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.entity.Student;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}
