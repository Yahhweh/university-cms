package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.repository.DurationRepository;
import placeholder.organisation.unicms.entity.Duration;

import java.time.LocalTime;
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
        validateDuration(duration);
        durationRepository.save(duration);
        log.info("Duration saved successfully. Start: {}, End: {}", duration.getStart(), duration.getEnd());
    }

    @Transactional
    public void removeDuration(long durationId){
        try {
            Optional<Duration> duration = durationRepository.findById(durationId);
            duration.ifPresent(durationRepository::delete);
        }catch (RuntimeException e){
            log.error("Failed to delete duration with id: {}", durationId);
            throw new ServiceException("Error deleting duration");
        }
    }

    private boolean isStartBeforeEnd(LocalTime start, LocalTime end){
        return start.isBefore(end);
    }

    private boolean isDurationInRange(LocalTime start, LocalTime end){
        java.time.Duration duration = java.time.Duration.between(start, end);

        return duration.toMinutes() >= 45 && duration.toMinutes() <= 90;
    }

    private void validateDuration(Duration duration) {
        if (!isStartBeforeEnd(duration.getStart(), duration.getEnd())) {
            throw new ServiceException("Start time must be before end time");
        }
        if (!isDurationInRange(duration.getStart(), duration.getEnd())) {
            throw new ServiceException("Duration must be between 45 and 90 minutes");
        }
    }
}
