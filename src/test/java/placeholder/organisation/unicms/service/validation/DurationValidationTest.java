package placeholder.organisation.unicms.service.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.excpetion.EntityNotFoundException;
import placeholder.organisation.unicms.excpetion.EntityValidationException;
import placeholder.organisation.unicms.excpetion.ServiceException;
import placeholder.organisation.unicms.service.dto.DurationDTO;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DurationValidationTest {
    @InjectMocks
    DurationValidation durationValidation;

    @Test
    void validateDuration_doesNotThrowsException_whenDurationIsBetween45And90(){
        Duration duration = getDuration();
        assertDoesNotThrow(() -> durationValidation.validateDuration(duration));
    }

    @Test
    void validateDuration_ThrowsException_whenDurationIsMoreThan90(){
        Duration duration = getDuration();
        duration.setEnd(LocalTime.of(14, 00));
        assertThrows(EntityValidationException.class, () -> durationValidation.validateDuration(duration));
    }

    @Test
    void validateDuration_ThrowsException_whenStartAfterEnd(){
        Duration duration = getDuration();
        duration.setStart(LocalTime.of(15, 20));

        assertThrows(EntityValidationException.class, () -> durationValidation.validateDuration(duration));
    }

    Duration getDuration(){
        return new Duration(1L, LocalTime.of(8, 30), LocalTime.of(10, 00));
    }
    DurationDTO getDurationDto(){
        return new DurationDTO(LocalTime.of(10, 00), LocalTime.of(11, 30));
    }
}