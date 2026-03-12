package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.Subject;
import placeholder.organisation.unicms.service.dto.response.SubjectResponseDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StudySubjectMapper {
    SubjectResponseDTO toDto(Subject subject);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(SubjectResponseDTO dto, @MappingTarget Subject entity);
}
