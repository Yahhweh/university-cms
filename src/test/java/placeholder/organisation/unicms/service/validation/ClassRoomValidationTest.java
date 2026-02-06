package placeholder.organisation.unicms.service.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.entity.ClassRoomType;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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

        boolean result = classRoomValidation.isClassRoomInCorrectCorpus(classRoom);

        assertThat(result).isTrue();
    }

    @Test
    void isClassRoomInCorrectCorpus_whenClassRoomTypeAndCorpusDontMatch_shouldReturnFalse() {
        when(classRoomType.getName()).thenReturn("Laboratory");
        when(classRoom.getClassRoomType()).thenReturn(classRoomType);
        when(classRoom.getRoom()).thenReturn("A101");

        boolean result = classRoomValidation.isClassRoomInCorrectCorpus(classRoom);

        assertThat(result).isFalse();
    }

    @Test
    void isClassRoomInCorrectCorpus_whenClassRoomTypeIsNull_shouldReturnFalse() {
        when(classRoom.getClassRoomType()).thenReturn(null);
        when(classRoom.getRoom()).thenReturn("A101");

        boolean result = classRoomValidation.isClassRoomInCorrectCorpus(classRoom);

        assertThat(result).isFalse();
    }
}