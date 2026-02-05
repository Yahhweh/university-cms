package placeholder.organisation.unicms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.entity.ClassRoomType;
import placeholder.organisation.unicms.repository.ClassRoomRepository;
import placeholder.organisation.unicms.repository.ClassRoomTypeRepository;
import placeholder.organisation.unicms.service.dto.ClassRoomDTO;
import placeholder.organisation.unicms.service.dto.ClassRoomTypeDTO;
import placeholder.organisation.unicms.service.mapper.ClassRoomMapper;
import placeholder.organisation.unicms.service.mapper.ClassRoomTypeMapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassRoomTypeServiceTest {
    @Spy
    ClassRoomTypeMapper classRoomTypeMapper = Mappers.getMapper(ClassRoomTypeMapper.class);
    @Mock
    ClassRoomTypeRepository classRoomTypeRepository;
    @InjectMocks
    ClassRoomTypeService classRoomTypeService;

    @Test
    void updateClassRoomType_changesObject_whenCorrectDtoIsGiven() {
        ClassRoomType initialClassRoomType = getClassRoomType();
        ClassRoomTypeDTO requestedChanges = getClassRoomTypeDTO();
        long id = initialClassRoomType.getId();

        when(classRoomTypeRepository.findById(id)).thenReturn(Optional.of(initialClassRoomType));

        classRoomTypeService.updateClassRoomType(id, requestedChanges);

        verify(classRoomTypeMapper).updateEntityFromDto(requestedChanges, initialClassRoomType);
        verify(classRoomTypeRepository).save(initialClassRoomType);

        assertThat(initialClassRoomType.getName()).isEqualTo(getClassRoomTypeDTO().getName());

    }

    ClassRoomType getClassRoomType() {
        return new ClassRoomType(1L, "Hall", 200L);
    }

    ClassRoomTypeDTO getClassRoomTypeDTO() {
        return new ClassRoomTypeDTO("Working room");
    }
}