package placeholder.organisation.unicms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.C;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.entity.ClassRoomType;
import placeholder.organisation.unicms.repository.ClassRoomRepository;
import placeholder.organisation.unicms.repository.ClassRoomTypeRepository;
import placeholder.organisation.unicms.service.dto.ClassRoomDTO;
import placeholder.organisation.unicms.service.mapper.ClassRoomMapper;
import placeholder.organisation.unicms.service.validation.ClassRoomValidator;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassRoomServiceTest {
    @Spy
    ClassRoomMapper classRoomMapper = Mappers.getMapper(ClassRoomMapper.class);
    @Mock
    ClassRoomRepository classRoomRepository;
    @Mock
    ClassRoomValidator classRoomValidator;
    @Mock
    ClassRoomTypeRepository classRoomTypeRepository;
    @InjectMocks
    ClassRoomService classRoomService;

    @Test
    void updateClassRoom_changeNumberOfRoom_whenDTOisGiven() {
        ClassRoom initialClassRoom = getClassRoom();
        ClassRoomDTO requestedChanges = getClassRoomDTO();
        requestedChanges.setClassRoomTypeId(4L);

        ClassRoomType newType = new ClassRoomType(4L, "Physics Lab", 100L);

        when(classRoomRepository.findById(initialClassRoom.getId()))
                .thenReturn(Optional.of(initialClassRoom));

        when(classRoomTypeRepository.getReferenceById(4L)).thenReturn(newType);

        classRoomService.updateClassRoom(initialClassRoom.getId(), requestedChanges);

        assertNotNull(initialClassRoom.getClassRoomType(), "ClassRoomType should not be null after update");
        assertThat(initialClassRoom.getClassRoomType().getId()).isEqualTo(4L);
        assertThat(initialClassRoom.getClassRoomType().getName()).isEqualTo("Physics Lab");
    }

    @Test
    void updateClassRoom_throwException_whenDTOIsNotValidated(){
        ClassRoomDTO classRoomDTO = getClassRoomDTO();
        ClassRoom classRoom = getClassRoom();
        classRoomDTO.setRoom("B-102");
        long id = classRoom.getId();

        when(classRoomRepository.findById(id)).thenReturn(Optional.of(classRoom));

        doThrow(EntityValidationException.class).when(classRoomValidator).validateClassRoom(classRoom);

        assertThrows(EntityValidationException.class, () -> classRoomService.updateClassRoom(id, classRoomDTO));
    }

    @Test
    void createClassRoom_shouldThrowEntityValidationException_whenWrongClassRoomTypeGiven(){
        ClassRoom classRoom = getClassRoom();
        classRoom.setClassRoomType(new ClassRoomType(2L, "Smth", 2L));

        doThrow(EntityValidationException.class).when(classRoomValidator).validateClassRoom(classRoom);

        assertThrows(EntityValidationException.class, () -> classRoomService.createClassRoom(classRoom));
    }

    @Test
    void createClassRoom_shouldSave_whenCorrectClassRoomTypeGiven(){
        ClassRoom classRoom = getClassRoom();

        assertDoesNotThrow( () -> classRoomService.createClassRoom(classRoom));

        verify(classRoomValidator).validateClassRoom(classRoom);
        verify(classRoomRepository).save(classRoom);
    }



    @Test
    void removeClassRoom_shouldRemoveClassRoom_WhenClassRoomExists(){
        ClassRoom classRoom = getClassRoom();

        when(classRoomRepository.existsById(classRoom.getId())).thenReturn(true);

        classRoomService.removeClassRoom(classRoom.getId());
        
        verify(classRoomRepository).deleteById(classRoom.getId());
    }
    
    @Test
    void removeClassRoom_shouldThrowEntityNotFound_WhenClassRoomDoesNotExist(){
        long id = 22L;

        when(classRoomRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,()-> classRoomService.removeClassRoom(id));
        verify(classRoomRepository).existsById(id);
    }
    

    ClassRoom getClassRoom() {
        return new ClassRoom(1L, "A-101", new ClassRoomType(1L, "Hall", 100L));
    }

    ClassRoomDTO getClassRoomDTO() {
        return new ClassRoomDTO("B-102");
    }
}