package placeholder.organisation.unicms.service.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.service.EntityValidationException;

import java.time.LocalTime;

@Component
public class DurationValidator {

    private final int minDurationMinutes;
    private final int maxDurationMinutes;

    public DurationValidator(@Value("${lesson.validation.min_duration_minutes}") int minDurationMinutes,
                             @Value("${lesson.validation.max_duration_minutes}") int maxDurationMinutes) {
        this.minDurationMinutes = minDurationMinutes;
        this.maxDurationMinutes = maxDurationMinutes;
    }

    public void validateDuration(Duration duration) {
        if (!isStartBeforeEnd(duration.getStart(), duration.getEnd())) {
            throw new EntityValidationException("Start time must be before end time", Duration.class, String.valueOf(duration.getId()));
        }
        if (!isDurationInRange(duration.getStart(), duration.getEnd())) {
            String message = String.format("Duration must be between %d and %d minutes",minDurationMinutes, maxDurationMinutes);
            throw new EntityValidationException(message, Duration.class, String.valueOf(duration.getId()));
        }
    }

    private boolean isStartBeforeEnd(LocalTime start, LocalTime end) {
        return start.isBefore(end);
    }

    private boolean isDurationInRange(LocalTime start, LocalTime end) {
        java.time.Duration duration = java.time.Duration.between(start, end);

        return duration.toMinutes() >= minDurationMinutes &&
        duration.toMinutes() <= maxDurationMinutes;
    }
}
