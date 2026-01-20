package placeholder.organisation.unicms.service.datagenerator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.service.GroupService;
import placeholder.organisation.unicms.service.StudentService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentsGeneratorTest {

    @Mock
    private StudentService mockStudentService;
    @Mock
    private GroupService mockGroupService;
    @InjectMocks
    private StudentsGenerator studentsGenerator;

    @Test
    void generate_shouldCreateExactAmountOfStudents() {
        int amount = 15;
        studentsGenerator.generate(amount);
        verify(mockStudentService, times(amount)).createStudent(any(Student.class));
    }

    @Test
    void assignRandomGroups_shouldUpdateExpectedNumberOfStudents() {
        int studentCount = 10;
        int minPerGroup = 2;
        int maxPerGroup = 2;

        List<Student> mockStudents = createMockStudents(studentCount);
        List<Group> mockGroups = createMockGroups(3);

        when(mockStudentService.findAllStudents()).thenReturn(mockStudents);
        when(mockGroupService.findAllGroups()).thenReturn(mockGroups);

        studentsGenerator.assignRandomGroups(minPerGroup, maxPerGroup);

        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(mockStudentService, times(6)).updateStudent(studentCaptor.capture());

        long uniqueStudentsAssigned = studentCaptor.getAllValues().stream()
                .map(Student::getId)
                .distinct()
                .count();

        assertEquals(6, uniqueStudentsAssigned);
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