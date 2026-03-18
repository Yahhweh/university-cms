package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.service.dto.request.StudentRequestDTO;
import placeholder.organisation.unicms.service.dto.response.StudentResponseDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StudentMapper {
    StudentResponseDTO toDto(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", ignore = true)
    void updateEntityFromDto(StudentResponseDTO dto, @MappingTarget Student entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", ignore = true)
    void updateEntityFromDto(StudentRequestDTO dto, @MappingTarget Student entity);

    Student toEntity(StudentRequestDTO requestDTO);

    Student toEntity(StudentResponseDTO studentResponseDTO);
}
