package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.repository.DurationRepository;
import placeholder.organisation.unicms.entity.Duration;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class DurationService {

    private final DurationRepository durationRepository;

    public DurationService(DurationRepository durationRepository) {
        this.durationRepository = durationRepository;
    }

    public List<Duration> findAllDurations() {
        List<Duration> durations = durationRepository.findAll();
        log.debug("Found {} durations ", durations.size());
        return durations;
    }

    @Transactional
    public void createDuration(Duration duration) {
        durationRepository.save(duration);
        log.info("Duration saved successfully. Start: {}, End: {}", duration.getStart(), duration.getEnd());
    }

    public void removeDuration(long durationId){
        try {
            Optional<Duration> duration = durationRepository.findById(durationId);
            duration.ifPresent(durationRepository.delete(duration));
        }catch (RuntimeException e){
            log.error("Failed to delete duration with id: {}", durationId);
            throw new ServiceException("Error deleting duration");
        }
    }
}
