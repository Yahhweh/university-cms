package placeholder.organisation.unicms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.Room;
import placeholder.organisation.unicms.entity.RoomType;
import placeholder.organisation.unicms.repository.RoomRepository;
import placeholder.organisation.unicms.repository.RoomTypeRepository;
import placeholder.organisation.unicms.service.dto.RoomDTO;
import placeholder.organisation.unicms.service.mapper.ClassRoomMapper;
import placeholder.organisation.unicms.service.validation.ClassRoomValidator;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {
    @Spy
    ClassRoomMapper classRoomMapper = Mappers.getMapper(ClassRoomMapper.class);
    @Mock
    RoomRepository roomRepository;
    @Mock
    ClassRoomValidator classRoomValidator;
    @Mock
    RoomTypeRepository roomTypeRepository;
    @InjectMocks
    RoomService roomService;

    @Test
    void updateClassRoom_shouldChangeNumberOfRoom_whenDTOisGiven() {
        Room initialRoom = getClassRoom();
        RoomDTO requestedChanges = getClassRoomDTO();
        requestedChanges.setClassRoomTypeId(4L);

        RoomType newType = new RoomType(4L, "Physics Lab", 100L);

        when(roomRepository.findById(initialRoom.getId()))
                .thenReturn(Optional.of(initialRoom));

        when(roomTypeRepository.getReferenceById(4L)).thenReturn(newType);

        roomService.updateClassRoom(initialRoom.getId(), requestedChanges);

        assertNotNull(initialRoom.getRoomType(), "ClassRoomType should not be null after update");
        assertThat(initialRoom.getRoomType().getId()).isEqualTo(4L);
        assertThat(initialRoom.getRoomType().getName()).isEqualTo("Physics Lab");
    }

    @Test
    void updateClassRoom_shouldThrowException_whenDTOIsNotValidated(){
        RoomDTO roomDTO = getClassRoomDTO();
        Room room = getClassRoom();
        roomDTO.setRoom("B-102");
        long id = room.getId();

        when(roomRepository.findById(id)).thenReturn(Optional.of(room));

        doThrow(EntityValidationException.class).when(classRoomValidator).validateClassRoom(room);

        assertThrows(EntityValidationException.class, () -> roomService.updateClassRoom(id, roomDTO));
    }

    @Test
    void createClassRoom_shouldThrowEntityValidationException_whenWrongClassRoomTypeGiven(){
        Room room = getClassRoom();
        room.setRoomType(new RoomType(2L, "Smth", 2L));

        doThrow(EntityValidationException.class).when(classRoomValidator).validateClassRoom(room);

        assertThrows(EntityValidationException.class, () -> roomService.createClassRoom(room));
    }

    @Test
    void createClassRoom_shouldSave_whenCorrectClassRoomTypeGiven(){
        Room room = getClassRoom();

        assertDoesNotThrow( () -> roomService.createClassRoom(room));

        verify(classRoomValidator).validateClassRoom(room);
        verify(roomRepository).save(room);
    }



    @Test
    void removeClassRoom_shouldRemoveClassRoom_WhenClassRoomExists(){
        Room room = getClassRoom();

        when(roomRepository.existsById(room.getId())).thenReturn(true);

        roomService.removeClassRoom(room.getId());
        
        verify(roomRepository).deleteById(room.getId());
    }
    
    @Test
    void removeClassRoom_shouldThrowEntityNotFound_WhenClassRoomDoesNotExist(){
        long id = 22L;

        when(roomRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,()-> roomService.removeClassRoom(id));
        verify(roomRepository).existsById(id);
    }
    

    Room getClassRoom() {
        return new Room(1L, "A-101", new RoomType(1L, "Hall", 100L));
    }

    RoomDTO getClassRoomDTO() {
        return new RoomDTO("B-102");
    }
}