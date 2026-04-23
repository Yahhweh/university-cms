package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import placeholder.organisation.unicms.entity.Schedule;
import placeholder.organisation.unicms.service.ScheduleService;
import placeholder.organisation.unicms.service.dto.request.ScheduleRequestDTO;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ScheduleMapper {
    Schedule toEntity(ScheduleRequestDTO dto);

    void updateEntityFromDto(ScheduleRequestDTO dto, @MappingTarget Schedule entity);
}
