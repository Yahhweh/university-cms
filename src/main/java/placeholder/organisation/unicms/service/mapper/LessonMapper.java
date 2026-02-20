package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.entity.Lesson;
import placeholder.organisation.unicms.entity.StudySubject;
import placeholder.organisation.unicms.repository.*;
import placeholder.organisation.unicms.service.dto.LessonDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LessonMapper {

    Lesson toEntity(LessonDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "duration", ignore = true)
    @Mapping(target = "studySubject", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "lecturer", ignore = true)
    @Mapping(target = "classRoom", ignore = true)
    void updateEntityFromDto(LessonDTO dto, @MappingTarget Lesson lesson);
}
