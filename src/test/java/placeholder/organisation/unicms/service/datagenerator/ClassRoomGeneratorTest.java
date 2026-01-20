package placeholder.organisation.unicms.service.datagenerator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.entity.ClassRoomType;
import placeholder.organisation.unicms.service.ClassRoomService;
import placeholder.organisation.unicms.service.ClassRoomTypeService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassRoomGeneratorTest {

    @Mock
    ClassRoomService classRoomServiceMock;

    @Mock
    ClassRoomTypeService classRoomTypeServiceMock;

    @InjectMocks
    ClassRoomGenerator classRoomGenerator;

    @Test
    void generate_CreatesSpecifiedNumberOfClassRooms_WhenAmountIsPositive() {
        int amount = 5;
        ClassRoomType type = new ClassRoomType("General", 30L);
        when(classRoomTypeServiceMock.findAllRoomTypes()).thenReturn(List.of(type));

        classRoomGenerator.generate(amount);

        verify(classRoomServiceMock, times(amount)).createClassRoom(any(ClassRoom.class));
    }

    @Test
    void generate_CreatesRoomsWithAPrefix_WhenCapacityIsGreaterThanFifty() {
        int amount = 1;
        ClassRoomType bigType = new ClassRoomType("Auditorium", 100L);
        when(classRoomTypeServiceMock.findAllRoomTypes()).thenReturn(List.of(bigType));

        classRoomGenerator.generate(amount);

        ArgumentCaptor<ClassRoom> roomCaptor = ArgumentCaptor.forClass(ClassRoom.class);
        verify(classRoomServiceMock).createClassRoom(roomCaptor.capture());
        assertTrue(roomCaptor.getValue().getRoom().startsWith("A-"));
    }

    @Test
    void generate_CreatesRoomsWithBPrefix_WhenCapacityIsFiftyOrLess() {
        int amount = 1;
        ClassRoomType smallType = new ClassRoomType("Classroom", 30L);
        when(classRoomTypeServiceMock.findAllRoomTypes()).thenReturn(List.of(smallType));

        classRoomGenerator.generate(amount);

        ArgumentCaptor<ClassRoom> roomCaptor = ArgumentCaptor.forClass(ClassRoom.class);
        verify(classRoomServiceMock).createClassRoom(roomCaptor.capture());
        assertTrue(roomCaptor.getValue().getRoom().startsWith("B-"));
    }

    @Test
    void generate_FormatsRoomNumberCorrectly_WhenFloorAndIndexAreCalculated() {
        int amount = 1;
        ClassRoomType type = new ClassRoomType();
        type.setName("Lab");
        type.setCapacity(20L);
        when(classRoomTypeServiceMock.findAllRoomTypes()).thenReturn(List.of(type));
        classRoomGenerator.generate(amount);

        ArgumentCaptor<ClassRoom> roomCaptor = ArgumentCaptor.forClass(ClassRoom.class);
        verify(classRoomServiceMock).createClassRoom(roomCaptor.capture());

        assertEquals("B-101", roomCaptor.getValue().getRoom());
    }

    @Test
    void generate_DoesNotSaveAnything_WhenNoClassRoomTypesFound() {
        int amount = 10;
        when(classRoomTypeServiceMock.findAllRoomTypes()).thenReturn(List.of());

        classRoomGenerator.generate(amount);

        verify(classRoomServiceMock, times(0)).createClassRoom(any(ClassRoom.class));
    }
}