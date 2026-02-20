package placeholder.organisation.unicms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
