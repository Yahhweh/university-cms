package placeholder.organisation.unicms.service.datagenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.service.LecturerService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LecturerGeneratorTest {

    @Mock
    private LecturerService lecturerService;

    private LecturerGenerator lecturerGenerator;

    @BeforeEach
    void setup() {
        lecturerGenerator = new LecturerGenerator(lecturerService);
    }

    @Test
    void generate_createsExactAmountOfLecturers() {
        int amount = 10;

        lecturerGenerator.generate(amount);

        verify(lecturerService, times(amount)).addLecturer(any(Lecturer.class));
    }

    @Test
    void generate_populatesInheritedAndSpecificFields() {
        lecturerGenerator.generate(1);

        ArgumentCaptor<Lecturer> lecturerCaptor = ArgumentCaptor.forClass(Lecturer.class);
        verify(lecturerService).addLecturer(lecturerCaptor.capture());

        Lecturer savedLecturer = lecturerCaptor.getValue();

        assertNotNull(savedLecturer.getName());
        assertNotNull(savedLecturer.getSureName());
        assertNotNull(savedLecturer.getPassword());
        assertNotNull(savedLecturer.getGender());
        assertNotNull(savedLecturer.getDateOfBirth());

        assertNotNull(savedLecturer.getSalary());
        assertTrue(savedLecturer.getSalary() >= 3000);
    }
}