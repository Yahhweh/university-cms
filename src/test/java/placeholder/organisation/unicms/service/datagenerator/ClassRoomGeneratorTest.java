package placeholder.organisation.unicms.service.datagenerator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.entity.ClassRoomType;
import placeholder.organisation.unicms.service.ClassRoomService;
import placeholder.organisation.unicms.service.ClassRoomTypeService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassRoomGeneratorTest {

    @Mock
    private ClassRoomService classRoomServiceMock;

    @Mock
    private ClassRoomTypeService classRoomTypeServiceMock;

    @InjectMocks
    private ClassRoomGenerator classRoomGenerator;

    @Test
    void generate_shouldCreateSpecifiedAmountWithCorrectNamingLogic() {
        ClassRoomType bigType = new ClassRoomType("Auditorium", 100L);
        when(classRoomTypeServiceMock.findAllRoomTypes()).thenReturn(List.of(bigType));

        classRoomGenerator.generate(3);

        verify(classRoomServiceMock, times(3)).createClassRoom(argThat(room -> {
            assertThat(room.getRoom()).startsWith("A-");
            return true;
        }));
    }

    @Test
    void generate_shouldApplyPrefixBAndCorrectFormatting_whenCapacityIsSmall() {
        ClassRoomType smallType = new ClassRoomType("Lab", 20L);
        when(classRoomTypeServiceMock.findAllRoomTypes()).thenReturn(List.of(smallType));

        classRoomGenerator.generate(1);

        verify(classRoomServiceMock).createClassRoom(argThat(room -> {
            assertThat(room.getRoom()).isEqualTo("B-101");
            return true;
        }));
    }

    @Test
    void generate_shouldNotInvokeSave_whenNoTypesAvailable() {
        when(classRoomTypeServiceMock.findAllRoomTypes()).thenReturn(List.of());

        classRoomGenerator.generate(10);

        verify(classRoomServiceMock, never()).createClassRoom(any(ClassRoom.class));
    }
}