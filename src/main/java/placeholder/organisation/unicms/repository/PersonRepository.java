package placeholder.organisation.unicms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.Lesson;
import placeholder.organisation.unicms.entity.Person;
import placeholder.organisation.unicms.entity.Role;
import placeholder.organisation.unicms.entity.Student;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {
    Optional<Person> findByEmail(String email);
    Page<Person> findByRole(Role role, Pageable pageable);
}