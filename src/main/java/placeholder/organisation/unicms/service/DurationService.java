package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.repository.DurationRepository;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.service.validation.DurationValidation;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class DurationService {

    private final DurationRepository durationRepository;
    private final DurationValidation durationValidation;

    public DurationService(DurationRepository durationRepository, DurationValidation durationValidation) {
        this.durationRepository = durationRepository;
        this.durationValidation = durationValidation;
    }

    public List<Duration> findAllDurations() {
        List<Duration> durations = durationRepository.findAll();
        log.debug("Found {} durations ", durations.size());
        return durations;
    }

    @Transactional
    public void createDuration(Duration duration) {
        durationValidation.validateDuration(duration);
        durationRepository.save(duration);
        log.info("Duration saved successfully. Start: {}, End: {}", duration.getStart(), duration.getEnd());
    }

    @Transactional
    public void removeDuration(long durationId) {
        if (!durationRepository.existsById(durationId)) {
            throw new ServiceException("Duration type not found with id: " + durationId);
        }
        durationRepository.deleteById(durationId);
    }
}
