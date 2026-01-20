package placeholder.organisation.unicms.service.datagenerator;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import placeholder.organisation.unicms.entity.GenderType;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.service.LecturerService;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
@Log4j2
public class LecturerGenerator implements DataGenerator {

    private static final List<String> FIRST_NAMES = List.of(
            "Liam", "Noah", "Oliver", "Elijah", "James",
            "William", "Benjamin", "Lucas", "Henry", "Alexander"
    );

    private static final List<String> LAST_NAMES = List.of(
            "Smith", "Johnson", "Williams", "Brown", "Jones",
            "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"
    );

    private final LecturerService lecturerService;
    private final Random random = new Random();

    public LecturerGenerator(LecturerService lecturerService) {
        this.lecturerService = lecturerService;
    }

    @Override
    public void generate(int amount) {
        log.info("Generating {} lecturers...", amount);
        for (int i = 0; i < amount; i++) {
            Lecturer lecturer = new Lecturer();

            lecturer.setName(FIRST_NAMES.get(random.nextInt(FIRST_NAMES.size())));
            lecturer.setSureName(LAST_NAMES.get(random.nextInt(LAST_NAMES.size())));
            lecturer.setPassword("password_" + i);
            lecturer.setGender(random.nextBoolean() ? GenderType.Male : GenderType.Female);
            lecturer.setDateOfBirth(LocalDate.now().minusYears(30 + random.nextInt(35)));

            lecturer.setSalary(3000 + random.nextInt(4000));

            lecturerService.addLecturer(lecturer);
        }
    }
}