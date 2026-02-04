package placeholder.organisation.unicms.service.validation;

import org.springframework.stereotype.Component;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.service.ServiceException;

import java.time.LocalTime;

@Component
public class DurationValidation {

    private boolean isStartBeforeEnd(LocalTime start, LocalTime end){
        return start.isBefore(end);
    }

    private boolean isDurationInRange(LocalTime start, LocalTime end){
        java.time.Duration duration = java.time.Duration.between(start, end);

        return duration.toMinutes() >= 45 && duration.toMinutes() <= 90;
    }

    public void validateDuration(Duration duration) {
        if (!isStartBeforeEnd(duration.getStart(), duration.getEnd())) {
            throw new ServiceException("Start time must be before end time");
        }
        if (!isDurationInRange(duration.getStart(), duration.getEnd())) {
            throw new ServiceException("Duration must be between 45 and 90 minutes");
        }
    }
}
