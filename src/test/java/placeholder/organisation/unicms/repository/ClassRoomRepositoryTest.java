package placeholder.organisation.unicms.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.jdbc.Sql;
import placeholder.organisation.unicms.entity.ClassRoom;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ClassRoomRepository.class))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/datasets/class_room_jpa.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ClassRoomRepositoryTest {

    @Autowired
    private ClassRoomRepository classRoomRepository;

    private static final LocalDate TEST_DATE = LocalDate.of(2025, 10, 29);

    @Test
    @DisplayName("Should validate isClassRoomFree method logic (NOT EXISTS clause)")
    void isClassRoomFree_ReturnsConsistentResults() {
        LocalTime startTime = LocalTime.of(13, 00);
        LocalTime endTime = LocalTime.of(14, 30);

        List<ClassRoom> freeRooms = classRoomRepository.isClassRoomFree(TEST_DATE, startTime, endTime);

        assertNotNull(freeRooms);
        assertFalse(freeRooms.isEmpty());
    }
}