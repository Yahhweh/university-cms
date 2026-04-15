package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.service.dto.request.DurationRequestDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DurationMapper {
    DurationRequestDTO toDto(Duration duration);

    Duration toEntity(DurationRequestDTO requestDTO);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(DurationRequestDTO dto, @MappingTarget Duration entity);
}
