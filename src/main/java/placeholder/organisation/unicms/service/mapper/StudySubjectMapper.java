package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.StudySubject;
import placeholder.organisation.unicms.service.dto.StudySubjectDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StudySubjectMapper {
    StudySubjectDTO toDto(StudySubject studySubject);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(StudySubjectDTO dto, @MappingTarget StudySubject entity);
}
