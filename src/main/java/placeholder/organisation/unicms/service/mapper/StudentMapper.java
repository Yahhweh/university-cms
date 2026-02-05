package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.service.dto.StudentDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StudentMapper {
    StudentDTO toDto(Student student);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(StudentDTO dto, @MappingTarget Student entity);
}
