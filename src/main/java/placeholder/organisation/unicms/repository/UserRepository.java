package placeholder.organisation.unicms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.entity.User;
import placeholder.organisation.unicms.entity.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    @Override
    @EntityGraph(attributePaths = {"roles"})
    Page<User> findAll(Specification<User> spec, Pageable pageable);

    Page<User> findByRolesContaining(Role role, Pageable pageable);
}