package placeholder.organisation.unicms.service.datagenerator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.ClassRoomType;
import placeholder.organisation.unicms.service.ClassRoomTypeService;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassRoomTypeGeneratorTest {

    @Mock
    private ClassRoomTypeService classRoomTypeServiceMock;

    @InjectMocks
    private ClassRoomTypeGenerator classRoomTypeGenerator;

    @Test
    void generate_CreatesSpecifiedNumberOfClassRoomTypes_WhenAmountIsPositive() {
        int amount = 5;
        classRoomTypeGenerator.generate(amount);
        verify(classRoomTypeServiceMock, times(5)).createClassroomType(any(ClassRoomType.class));
    }

    @Test
    void generate_HandlesAmountExceedingInternalListSize_UsingModulo() {
        int amount = 20;

        classRoomTypeGenerator.generate(amount);

        verify(classRoomTypeServiceMock, times(20)).createClassroomType(any(ClassRoomType.class));
    }

    @Test
    void generate_DoesNothing_WhenAmountIsZero() {
        classRoomTypeGenerator.generate(0);
        verify(classRoomTypeServiceMock, never()).createClassroomType(any(ClassRoomType.class));
    }
}