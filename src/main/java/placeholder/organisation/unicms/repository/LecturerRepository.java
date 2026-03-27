package placeholder.organisation.unicms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.Lecturer;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Long>, JpaSpecificationExecutor<Lecturer> {
    Optional<Lecturer> findByNameAndSureName(String name, String sureName);

    Optional<Lecturer> findByEmail(String email);

    @Query("SELECT DISTINCT lec FROM Lecturer lec JOIN lec.subjects s WHERE s.id = :subjectId")
    Optional<List<Lecturer>> findLecturerBySubjectId(@Param("subjectId") Long subjectId);
}
