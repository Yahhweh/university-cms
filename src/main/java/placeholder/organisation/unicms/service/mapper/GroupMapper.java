package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.service.createDTO.GroupDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface GroupMapper {
    GroupDTO toDto(Group group);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(GroupDTO dto, @MappingTarget Group entity);
}
