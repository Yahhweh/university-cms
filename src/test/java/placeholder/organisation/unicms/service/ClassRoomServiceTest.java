package placeholder.organisation.unicms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.entity.ClassRoomType;
import placeholder.organisation.unicms.repository.ClassRoomRepository;
import placeholder.organisation.unicms.service.dto.ClassRoomDTO;
import placeholder.organisation.unicms.service.mapper.ClassRoomMapper;
import placeholder.organisation.unicms.service.validation.ClassRoomValidation;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.in;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    ClassRoom getClassRoom() {
        return new ClassRoom(1L, "A-101", new ClassRoomType(1L, "Hall", 100L));
    }

    ClassRoomDTO getClassRoomDTO() {
        return new ClassRoomDTO("A-102");
    }
}