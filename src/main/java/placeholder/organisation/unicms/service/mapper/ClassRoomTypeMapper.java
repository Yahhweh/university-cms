package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.RoomType;
import placeholder.organisation.unicms.service.dto.request.RoomTypeRequestDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, componentModel = "spring")
public interface ClassRoomTypeMapper {
    RoomTypeRequestDTO toDto(RoomType classRoom);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(RoomTypeRequestDTO dto, @MappingTarget RoomType entity);
}
