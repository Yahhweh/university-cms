package placeholder.organisation.unicms.service.validation;

import jakarta.xml.bind.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.entity.ClassRoomType;
import placeholder.organisation.unicms.excpetion.EntityValidationException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassRoomValidationTest {
    @Mock
    private ClassRoom classRoom;
    @Mock
    private ClassRoomType classRoomType;
    @InjectMocks
    private ClassRoomValidation classRoomValidation;

    @Test
    void isClassRoomInCorrectCorpus_whenClassRoomTypeAndCorpusMatch_shouldReturnTrue() {
        when(classRoomType.getName()).thenReturn("Hall");
        when(classRoom.getClassRoomType()).thenReturn(classRoomType);
        when(classRoom.getRoom()).thenReturn("A101");

        assertDoesNotThrow(() ->classRoomValidation.validateClassRoom(classRoom));
    }

    @Test
    void isClassRoomInCorrectCorpus_whenClassRoomTypeAndCorpusDontMatch_shouldReturnFalse() {
        when(classRoomType.getName()).thenReturn("Laboratory");
        when(classRoom.getClassRoomType()).thenReturn(classRoomType);
        when(classRoom.getRoom()).thenReturn("A101");


        assertThrows(EntityValidationException.class,() ->classRoomValidation.validateClassRoom(classRoom));
    }

    @Test
    void isClassRoomInCorrectCorpus_whenClassRoomTypeIsNull_shouldThrowException() {
        when(classRoom.getClassRoomType()).thenReturn(null);
        when(classRoom.getRoom()).thenReturn("A101");


        assertThrows(EntityValidationException.class, () ->classRoomValidation.validateClassRoom(classRoom));
    }
}