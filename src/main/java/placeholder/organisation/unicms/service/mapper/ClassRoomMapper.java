package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import placeholder.organisation.unicms.entity.Room;
import placeholder.organisation.unicms.service.dto.request.RoomRequestDTO;

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClassRoomMapper {
    RoomRequestDTO toDto(Room room);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(RoomRequestDTO dto, @MappingTarget Room entity);
}
