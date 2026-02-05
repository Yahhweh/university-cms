package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.service.dto.GroupDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GroupMapper {
    GroupDTO toDto(Group group);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(GroupDTO dto, @MappingTarget Group entity);
}
