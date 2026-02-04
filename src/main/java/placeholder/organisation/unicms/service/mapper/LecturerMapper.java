package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.service.createDTO.LecturerDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface LecturerMapper {
    LecturerDTO toDto(Lecturer lecturer);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(LecturerDTO dto, @MappingTarget Lecturer entity);
}
