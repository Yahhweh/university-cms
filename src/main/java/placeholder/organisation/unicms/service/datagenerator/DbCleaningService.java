package placeholder.organisation.unicms.service.datagenerator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DbCleaningService {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void deleteAll() {
        String sql = "TRUNCATE TABLE lesson, lecturer_study_subject, student, lecturer, " +
                "person, address, \"group\", class_room, class_room_type, " +
                "duration, study_subject RESTART IDENTITY CASCADE";
        jdbcTemplate.execute(sql);
    }
}