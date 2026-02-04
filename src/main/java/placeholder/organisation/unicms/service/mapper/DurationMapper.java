package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.service.createDTO.DurationDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface DurationMapper {
    DurationDTO toDto(Duration duration);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(DurationDTO dto, @MappingTarget Duration entity);
}
