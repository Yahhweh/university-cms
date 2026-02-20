package placeholder.organisation.unicms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.Duration;

import java.util.Optional;
import java.util.function.Consumer;

@Repository
public interface DurationRepository extends JpaRepository<Duration, Long> {
}
