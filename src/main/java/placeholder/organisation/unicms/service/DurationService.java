package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.dao.DurationJpa;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.entity.Duration;

import java.util.List;

@Service
@Log4j2
@Transactional(readOnly = true)
public class DurationService {

    DurationJpa durationJpa;

    public DurationService(DurationJpa durationJpa) {
        this.durationJpa = durationJpa;
    }

    public List<Duration> findAllDurations(){
       List<Duration> durations=  durationJpa.findAll();
        log.debug("Found {} durations ", durations.size());
        return durations;
    }

    @Transactional
    public void addDuration(Duration duration){
        durationJpa.save(duration);
        log.info("Duration saved successfully. Start: {}, End: {}", duration.getStart(), duration.getEnd());
    }
}
