package placeholder.organisation.unicms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.StudySubject;

import java.util.Optional;

@Repository
public interface StudySubjectDao extends JpaRepository<StudySubject, Long> {
    Optional<StudySubject> findByName(String name);
}
