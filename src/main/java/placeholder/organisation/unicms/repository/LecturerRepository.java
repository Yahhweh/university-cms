package placeholder.organisation.unicms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.Lecturer;

import java.util.List;
import java.util.Optional;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Long>, JpaSpecificationExecutor<Lecturer> {
    Optional<Lecturer> findByNameAndSureName(String name, String sureName);

    Optional<Lecturer> findByEmail(String email);

    List<Lecturer> findDistinctBySubjectsId(Long subjectId);
}