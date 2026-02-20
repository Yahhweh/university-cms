package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.service.dto.ClassRoomDTO;

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClassRoomMapper {
    ClassRoomDTO toDto(ClassRoom classRoom);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(ClassRoomDTO dto, @MappingTarget ClassRoom entity);
}
