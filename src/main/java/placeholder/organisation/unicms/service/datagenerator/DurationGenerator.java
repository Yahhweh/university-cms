package placeholder.organisation.unicms.service.datagenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.service.DurationService;

import java.time.LocalTime;

@Service
@Log4j2
public class DurationGenerator implements DataGenerator {

    private static final LocalTime START_DAY_TIME = LocalTime.of(8, 30);
    private static final LocalTime END_DAY_LIMIT = LocalTime.of(20, 0);
    private static final int LESSON_MINUTES = 90;
    private static final int BREAK_MINUTES = 15;

    private final DurationService durationService;

    public DurationGenerator(DurationService durationService) {
        this.durationService = durationService;
    }

    @Override
    public void generate(int amount) {
        log.info("Generating {} durations starting from {}", amount, START_DAY_TIME);
        LocalTime currentStart = START_DAY_TIME;
        for (int i = 0; i < amount; i++) {
            LocalTime currentEnd = currentStart.plusMinutes(LESSON_MINUTES);

            if (currentEnd.isAfter(END_DAY_LIMIT)) {
                log.warn("Maximum time reached. Stopping generation at index {}", i);
                break;
            }

            Duration duration = new Duration();
            duration.setStart(currentStart);
            duration.setEnd(currentEnd);

            durationService.addDuration(duration);

            currentStart = currentEnd.plusMinutes(BREAK_MINUTES);
        }
    }
}