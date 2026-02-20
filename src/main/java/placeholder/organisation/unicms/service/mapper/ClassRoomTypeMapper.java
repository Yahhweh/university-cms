package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.RoomType;
import placeholder.organisation.unicms.service.dto.RoomTypeDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, componentModel = "spring")
public interface ClassRoomTypeMapper {
    RoomTypeDTO toDto(RoomType classRoom);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(RoomTypeDTO dto, @MappingTarget RoomType entity);
}
