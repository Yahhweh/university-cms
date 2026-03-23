package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.service.dto.request.GroupRequestDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GroupMapper {
    GroupRequestDTO toDto(Group group);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(GroupRequestDTO dto, @MappingTarget Group entity);
}
