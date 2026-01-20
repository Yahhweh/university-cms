package placeholder.organisation.unicms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.Group;

@Repository
public interface GroupJpa extends JpaRepository<Group, Long> {
}
