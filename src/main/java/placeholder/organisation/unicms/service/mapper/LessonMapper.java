package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import placeholder.organisation.unicms.entity.Lesson;
import placeholder.organisation.unicms.service.createDTO.LessonDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface LessonMapper {
    LessonDTO toDto(Lesson lesson);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(LessonDTO dto, @MappingTarget Lesson entity);
}
