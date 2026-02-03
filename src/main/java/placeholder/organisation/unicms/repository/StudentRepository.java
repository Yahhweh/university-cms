package placeholder.organisation.unicms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.Student;

import java.util.Optional;
import java.util.function.Consumer;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Consumer<? super Student> delete(Optional<Student> student);
}
