package placeholder.organisation.unicms.service.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.service.EntityValidationException;
import placeholder.organisation.unicms.service.dto.DurationDTO;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DurationValidatorTest {

    private final DurationValidator durationValidator = new DurationValidator(45, 90);

    @Test
    void validateDuration_shouldNotThrowException_whenDurationIsValid() {
        Duration duration = getDuration();
        assertDoesNotThrow(() -> durationValidator.validateDuration(duration));
    }

    @Test
    void validateDuration_shouldThrowException_whenStartIsAfterEnd() {
        Duration duration = getDuration();
        duration.setStart(LocalTime.of(11, 0));
        duration.setEnd(LocalTime.of(10, 0));

        assertThrows(EntityValidationException.class, () -> durationValidator.validateDuration(duration));
    }

    @Test
    void validateDuration_shouldThrowException_whenDurationIsLessThan45Minutes() {
        Duration duration = getDuration();
        duration.setEnd(LocalTime.of(9, 0));

        assertThrows(EntityValidationException.class, () -> durationValidator.validateDuration(duration));
    }

    private Duration getDuration() {
        return new Duration(1L, LocalTime.of(8, 30), LocalTime.of(10, 0));
    }
}