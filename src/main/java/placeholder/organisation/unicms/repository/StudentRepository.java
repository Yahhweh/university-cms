package placeholder.organisation.unicms.repository;


import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.Student;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    @EntityGraph(attributePaths = {"roles", "group", "group.course"})
    List<Student> findStudentsByGroupId(Long groupId);

    @Override
    @EntityGraph(attributePaths = {"roles"})
    List<Student> findAll(Specification<Student> spec);

    Optional<Student> findByEmail(String email);
}
