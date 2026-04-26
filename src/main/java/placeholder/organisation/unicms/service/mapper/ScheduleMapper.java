package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.Schedule;
import placeholder.organisation.unicms.service.dto.request.ScheduleRequestDTO;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ScheduleMapper {

    @Mapping(target = "duration.id", source = "durationId")
    @Mapping(target = "subject.id", source = "subjectId")
    @Mapping(target = "group.id", source = "groupId")
    @Mapping(target = "lecturer.id", source = "lecturerId")
    Schedule toEntity(ScheduleRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "duration", ignore = true)
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "lecturer", ignore = true)
    void updateEntityFromDto(ScheduleRequestDTO dto, @MappingTarget Schedule entity);
}
