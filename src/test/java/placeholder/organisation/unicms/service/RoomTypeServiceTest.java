package placeholder.organisation.unicms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.RoomType;
import placeholder.organisation.unicms.repository.RoomTypeRepository;
import placeholder.organisation.unicms.service.dto.RoomTypeDTO;
import placeholder.organisation.unicms.service.mapper.ClassRoomTypeMapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomTypeServiceTest {
    @Spy
    ClassRoomTypeMapper classRoomTypeMapper = Mappers.getMapper(ClassRoomTypeMapper.class);
    @Mock
    RoomTypeRepository roomTypeRepository;
    @InjectMocks
    RoomTypeService roomTypeService;

    @Test
    void updateClassRoomType_shouldChangesObject_whenCorrectDtoIsGiven() {
        RoomType initialRoomType = getClassRoomType();
        RoomTypeDTO requestedChanges = getClassRoomTypeDTO();
        long id = initialRoomType.getId();

        when(roomTypeRepository.findById(id)).thenReturn(Optional.of(initialRoomType));

        roomTypeService.updateClassRoomType(id, requestedChanges);

        verify(classRoomTypeMapper).updateEntityFromDto(requestedChanges, initialRoomType);
        verify(roomTypeRepository).save(initialRoomType);

        assertThat(initialRoomType.getName()).isEqualTo(getClassRoomTypeDTO().getName());
    }

    @Test
    void createClassRoomType_shouldSave_whenCorrectClassRoomTypeGiven() {
        RoomType roomType = getClassRoomType();

        assertDoesNotThrow(() -> roomTypeService.createClassroomType(roomType));

        verify(roomTypeRepository).save(roomType);
    }

    @Test
    void removeClassRoomType_shouldRemoveClassRoomType_WhenClassRoomTypeExists() {
        RoomType roomType = getClassRoomType();

        when(roomTypeRepository.existsById(roomType.getId())).thenReturn(true);

        roomTypeService.removeClassRoomType(roomType.getId());

        verify(roomTypeRepository).deleteById(roomType.getId());
    }

    @Test
    void removeClassRoomType_shouldThrowEntityNotFound_WhenClassRoomTypeDoesNotExist() {
        long id = 22L;

        when(roomTypeRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> roomTypeService.removeClassRoomType(id));
        verify(roomTypeRepository).existsById(id);
    }

    RoomType getClassRoomType() {
        return new RoomType(1L, "Hall", 200L);
    }

    RoomTypeDTO getClassRoomTypeDTO() {
        return new RoomTypeDTO("Working room");
    }
}