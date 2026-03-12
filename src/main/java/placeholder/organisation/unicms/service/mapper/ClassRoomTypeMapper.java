package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.RoomType;
import placeholder.organisation.unicms.service.dto.response.RoomTypeResponseDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, componentModel = "spring")
public interface ClassRoomTypeMapper {
    RoomTypeResponseDTO toDto(RoomType classRoom);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(RoomTypeResponseDTO dto, @MappingTarget RoomType entity);
}
