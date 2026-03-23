package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.service.dto.request.LecturerRequestDTO;
import placeholder.organisation.unicms.service.dto.response.LecturerResponseDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LecturerMapper {

    Lecturer toEntity(LecturerResponseDTO dto);

    Lecturer toEntity(LecturerRequestDTO dto);

    @Mapping(target = "subjects", ignore = true)
    void updateEntityFromDto(LecturerRequestDTO dto, @MappingTarget Lecturer entity);
}
