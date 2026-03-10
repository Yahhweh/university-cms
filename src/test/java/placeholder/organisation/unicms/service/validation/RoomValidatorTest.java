package placeholder.organisation.unicms.service.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.Room;
import placeholder.organisation.unicms.entity.RoomType;
import placeholder.organisation.unicms.service.EntityValidationException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomValidatorTest {
    @Mock
    private Room room;
    @Mock
    private RoomType roomType;
    @InjectMocks
    private ClassRoomValidator classRoomValidator;

    @Test
    void isClassRoomInCorrectCorpus_whenClassRoomTypeAndCorpusMatch_shouldReturnTrue() {
        when(roomType.getName()).thenReturn("Hall");
        when(room.getRoomType()).thenReturn(roomType);
        when(room.getRoom()).thenReturn("A101");

        assertDoesNotThrow(() -> classRoomValidator.validateClassRoom(room));
    }

    @Test
    void isClassRoomInCorrectCorpus_whenClassRoomTypeAndCorpusDontMatch_shouldReturnFalse() {
        when(roomType.getName()).thenReturn("Laboratory");
        when(room.getRoomType()).thenReturn(roomType);
        when(room.getRoom()).thenReturn("A101");


        assertThrows(EntityValidationException.class,() -> classRoomValidator.validateClassRoom(room));
    }

    @Test
    void isClassRoomInCorrectCorpus_whenClassRoomTypeIsNull_shouldThrowException() {
        when(room.getRoomType()).thenReturn(null);
        when(room.getRoom()).thenReturn("A101");


        assertThrows(EntityValidationException.class, () -> classRoomValidator.validateClassRoom(room));
    }

    @Test
    void isRoomIsFree(){

    }
}