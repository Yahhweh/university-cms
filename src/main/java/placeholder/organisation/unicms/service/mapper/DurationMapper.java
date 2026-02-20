package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.service.dto.DurationDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DurationMapper {
    DurationDTO toDto(Duration duration);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(DurationDTO dto, @MappingTarget Duration entity);
}
