package placeholder.organisation.unicms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.Lecturer;

import java.util.Optional;

@Repository
public interface LecturerDao extends JpaRepository<Lecturer, Long> {
    Optional<Lecturer> findByNameAndSureName(String name, String sureName);
}
