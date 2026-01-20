package placeholder.organisation.unicms.service.datagenerator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.service.DurationService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DurationGeneratorTest {

    @Mock
    private DurationService durationServiceMock;

    @InjectMocks
    private DurationGenerator durationGenerator;

    @Test
    void generate_SavesSpecifiedNumberOfDurations_WhenAmountIsPositive() {
        int amount = 5;
        durationGenerator.generate(amount);

        verify(durationServiceMock, times(amount)).addDuration(any(Duration.class));
    }

    @Test
    void generate_DoesNotSaveDuration_WhenAmountIsZero() {
        int amount = 0;
        durationGenerator.generate(amount);

        verify(durationServiceMock, times(0)).addDuration(any(Duration.class));
    }

    @Test
    void generate_StopsSavingDurations_WhenDailyLimitIsReached() {
        int excessiveAmount = 50;
        durationGenerator.generate(excessiveAmount);

        verify(durationServiceMock, atMost(10)).addDuration(any(Duration.class));
    }
}