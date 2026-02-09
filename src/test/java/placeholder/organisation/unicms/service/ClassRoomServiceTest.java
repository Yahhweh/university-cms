package placeholder.organisation.unicms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.C;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.entity.ClassRoomType;
import placeholder.organisation.unicms.excpetion.EntityNotFoundException;
import placeholder.organisation.unicms.excpetion.EntityValidationException;
import placeholder.organisation.unicms.repository.ClassRoomRepository;
import placeholder.organisation.unicms.service.dto.ClassRoomDTO;
import placeholder.organisation.unicms.service.mapper.ClassRoomMapper;
import placeholder.organisation.unicms.service.validation.ClassRoomValidation;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.in;
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
    ClassRoomValidation classRoomValidation;
    @InjectMocks
    ClassRoomService classRoomService;

    @Test
    void updateClassRoom_changeNumberOfRoom_whenDTOisGiven() {
        ClassRoom initialClassRoom = getClassRoom();
        Long id = initialClassRoom.getId();

        ClassRoomDTO requestedChanges = getClassRoomDTO();

        when(classRoomRepository.findById(id)).thenReturn(Optional.of(initialClassRoom));

        classRoomService.updateClassRoom(id, requestedChanges);

        assertThat(initialClassRoom.getRoom()).isEqualTo("A-102");
        verify(classRoomRepository).save(initialClassRoom);
    }

    @Test
    void createClassRoom_shouldThrowEntityValidationException_whenWrongClassRoomTypeGiven(){
        ClassRoom classRoom = getClassRoom();
        classRoom.setClassRoomType(new ClassRoomType(2L, "Smth", 2L));

        doThrow(EntityValidationException.class).when(classRoomValidation).validateClassRoom(classRoom);

        assertThrows(EntityValidationException.class, () -> classRoomService.createClassRoom(classRoom));
    }

    @Test
    void createClassRoom_shouldSave_whenCorrectClassRoomTypeGiven(){
        ClassRoom classRoom = getClassRoom();

        assertDoesNotThrow( () -> classRoomService.createClassRoom(classRoom));

        verify(classRoomValidation).validateClassRoom(classRoom);
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
        return new ClassRoomDTO("A-102");
    }
}