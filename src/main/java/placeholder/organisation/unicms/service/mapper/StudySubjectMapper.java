package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.Subject;
import placeholder.organisation.unicms.service.dto.SubjectDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StudySubjectMapper {
    SubjectDTO toDto(Subject subject);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(SubjectDTO dto, @MappingTarget Subject entity);
}
