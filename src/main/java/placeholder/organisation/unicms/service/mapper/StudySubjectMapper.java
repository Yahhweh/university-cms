package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import placeholder.organisation.unicms.entity.StudySubject;
import placeholder.organisation.unicms.service.createDTO.StudySubjectDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface StudySubjectMapper {
    StudySubjectDTO toDto(StudySubject studySubject);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(StudySubjectDTO dto, @MappingTarget StudySubject entity);
}
