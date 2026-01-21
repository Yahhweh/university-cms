package placeholder.organisation.unicms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.Person;

@Repository
public interface PersonDao extends JpaRepository<Person, Long> {
}
