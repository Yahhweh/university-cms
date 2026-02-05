package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.Lesson;
import placeholder.organisation.unicms.service.dto.LessonDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LessonMapper {
    LessonDTO toDto(Lesson lesson);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(LessonDTO dto, @MappingTarget Lesson entity);
}
