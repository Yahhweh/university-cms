package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.service.dto.response.GroupResponseDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GroupMapper {
    GroupResponseDTO toDto(Group group);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(GroupResponseDTO dto, @MappingTarget Group entity);
}
