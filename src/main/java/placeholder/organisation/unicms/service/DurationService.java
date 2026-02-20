package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.repository.DurationRepository;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.service.dto.DurationDTO;
import placeholder.organisation.unicms.service.mapper.DurationMapper;
import placeholder.organisation.unicms.service.validation.DurationValidator;

import java.util.List;

@Service
@Log4j2
@Transactional(readOnly = true)
public class DurationService {

    private final DurationRepository durationRepository;
    private final DurationValidator durationValidator;
    private final DurationMapper durationMapper;

    public DurationService(DurationRepository durationRepository, DurationValidator durationValidator, DurationMapper durationMapper) {
        this.durationRepository = durationRepository;
        this.durationValidator = durationValidator;
        this.durationMapper = durationMapper;
    }

    public List<Duration> findAllDurations() {
        List<Duration> durations = durationRepository.findAll();
        log.debug("Found {} durations", durations.size());
        return durations;
    }

    @Transactional
    public void createDuration(Duration duration) {
        durationValidator.validateDuration(duration);
        durationRepository.save(duration);
        log.info("Duration saved successfully. Start: {}, End: {}", duration.getStart(), duration.getEnd());
    }

    @Transactional
    public void removeDuration(long durationId) {
        if (!durationRepository.existsById(durationId)) {
            throw new EntityNotFoundException(Duration.class, String.valueOf(durationId));
        }
        durationRepository.deleteById(durationId);
    }

    @Transactional
    public void updateDuration(long durationId, DurationDTO durationDTO) {
        Duration duration = durationRepository.findById(durationId)
                .orElseThrow(() -> new EntityNotFoundException(Duration.class, String.valueOf(durationId)));

        durationMapper.updateEntityFromDto(durationDTO, duration);
        durationValidator.validateDuration(duration);

        durationRepository.save(duration);

        log.debug("Duration updated successfully. ID: {}", durationId);
    }
}