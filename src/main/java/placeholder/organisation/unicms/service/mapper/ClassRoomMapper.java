package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import placeholder.organisation.unicms.entity.Room;
import placeholder.organisation.unicms.service.dto.RoomDTO;

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClassRoomMapper {
    RoomDTO toDto(Room room);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(RoomDTO dto, @MappingTarget Room entity);
}
