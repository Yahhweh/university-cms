package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.Lesson;
import placeholder.organisation.unicms.service.dto.LessonDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LessonMapper {

    Lesson toEntity(LessonDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "duration", ignore = true)
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "lecturer", ignore = true)
    @Mapping(target = "room", ignore = true)
    void updateEntityFromDto(LessonDTO dto, @MappingTarget Lesson lesson);
}
