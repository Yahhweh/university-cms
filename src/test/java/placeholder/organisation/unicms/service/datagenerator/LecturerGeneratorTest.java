package placeholder.organisation.unicms.service.datagenerator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.Address;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.service.AddressService;
import placeholder.organisation.unicms.service.LecturerService;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LecturerGeneratorTest {

    @Mock
    private LecturerService lecturerServiceMock;
    @Mock
    private AddressService addressServiceMock;

    @InjectMocks
    private LecturerGenerator lecturerGenerator;

    @Test
    void generate_createsExactAmountOfLecturers() {
        int amount = 10;

        lecturerGenerator.generate(amount);

        verify(addressServiceMock).findAll();
        verify(lecturerServiceMock, times(amount)).addLecturer(any(Lecturer.class));
    }

    @Test
    void generate_populatesInheritedAndSpecificFields() {
        when(addressServiceMock.findAll()).thenReturn( List.of( new Address("1", "12", "1", "12", "12", "12")));
        lecturerGenerator.generate(1);

        verify(addressServiceMock).findAll();
        verify(lecturerServiceMock).addLecturer(argThat(lecturer -> {
            assertThat(lecturer.getName()).isNotNull();
            assertThat(lecturer.getSureName()).isNotNull();
            assertThat(lecturer.getPassword()).isNotNull();
            assertThat(lecturer.getGender()).isNotNull();
            assertThat(lecturer.getDateOfBirth()).isNotNull();
            assertThat(lecturer.getAddress()).isNotNull();

            assertThat(lecturer.getSalary()).isNotNull();
            assertThat(lecturer.getSalary()).isGreaterThanOrEqualTo(3000);

            return true;
        }));
    }
}