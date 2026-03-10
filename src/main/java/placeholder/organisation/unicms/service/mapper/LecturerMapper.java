package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.service.dto.LecturerDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LecturerMapper {

    Lecturer toEntity(LecturerDTO dto);

    @Mapping(target = "subjects", ignore = true)
    void updateEntityFromDto(LecturerDTO dto, @MappingTarget Lecturer entity);
}
