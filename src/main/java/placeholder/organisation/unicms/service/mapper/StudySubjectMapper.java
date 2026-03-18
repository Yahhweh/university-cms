package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.Subject;
import placeholder.organisation.unicms.service.dto.request.SubjectRequestDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StudySubjectMapper {
    SubjectRequestDTO toDto(Subject subject);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(SubjectRequestDTO dto, @MappingTarget Subject entity);
}
