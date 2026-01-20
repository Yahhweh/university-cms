package placeholder.organisation.unicms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.Duration;

@Repository
public interface DurationJpa extends JpaRepository<Duration, Long> {
}
