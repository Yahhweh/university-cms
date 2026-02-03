package placeholder.organisation.unicms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.Lecturer;

import java.util.Optional;
import java.util.function.Consumer;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Long> {
    Optional<Lecturer> findByNameAndSureName(String name, String sureName);

    Consumer<? super Lecturer> delete(Optional<Lecturer> lecturer);
}
