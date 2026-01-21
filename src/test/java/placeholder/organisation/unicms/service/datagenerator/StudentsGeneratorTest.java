package placeholder.organisation.unicms.service.datagenerator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.Address;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.service.AddressService;
import placeholder.organisation.unicms.service.GroupService;
import placeholder.organisation.unicms.service.StudentService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentsGeneratorTest {

    @Mock
    private StudentService mockStudentService;
    @Mock
    private GroupService mockGroupService;
    @Mock
    private AddressService mockAddressService;
    @InjectMocks
    private StudentsGenerator studentsGenerator;


    @Test
    void generate_shouldCreateExactAmountOfStudents() {
        int amount = 15;

        studentsGenerator.generate(amount);

        verify(mockAddressService).findAll();
        verify(mockStudentService, times(amount)).createStudent(any(Student.class));
    }

    @Test
    void assignRandomGroups_shouldUpdateExpectedNumberOfStudents() {
        int studentCount = 10;
        int minPerGroup = 2;
        int maxPerGroup = 2;
        int expectedAssignments = 6;

        List<Student> mockStudents = createMockStudents(studentCount);
        List<Group> mockGroups = createMockGroups(3);

        when(mockStudentService.findAllStudents()).thenReturn(mockStudents);
        when(mockGroupService.findAllGroups()).thenReturn(mockGroups);

        studentsGenerator.assignRandomGroups(minPerGroup, maxPerGroup);

        verify(mockStudentService, times(expectedAssignments)).updateStudent(any(Student.class));

        long assignedCount = mockStudents.stream()
                .filter(s -> s.getGroup() != null)
                .count();

        assertThat(assignedCount).isEqualTo(expectedAssignments);
    }

    private List<Student> createMockStudents(int count) {
        return LongStream.rangeClosed(1, count)
                .mapToObj(i -> {
                    Student s = new Student();
                    s.setId(i);
                    return s;
                }).collect(Collectors.toList());
    }

    private List<Group> createMockGroups(int count) {
        return LongStream.rangeClosed(1, count)
                .mapToObj(i -> {
                    Group g = new Group();
                    g.setId(i);
                    g.setName("Group-" + i);
                    return g;
                }).collect(Collectors.toList());
    }
}