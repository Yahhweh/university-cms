package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import placeholder.organisation.unicms.entity.Room;
import placeholder.organisation.unicms.service.dto.response.RoomResponseDTO;

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClassRoomMapper {
    RoomResponseDTO toDto(Room room);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(RoomResponseDTO dto, @MappingTarget Room entity);
}
