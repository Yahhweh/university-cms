package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import placeholder.organisation.unicms.entity.Course;
import placeholder.organisation.unicms.service.dto.request.CourseRequestDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CourseMapper {
    Course toEntity(CourseRequestDTO requestDTO);
}
