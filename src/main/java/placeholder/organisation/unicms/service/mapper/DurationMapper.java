package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.service.dto.response.DurationResponseDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DurationMapper {
    DurationResponseDTO toDto(Duration duration);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(DurationResponseDTO dto, @MappingTarget Duration entity);
}
