package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.ClassRoomType;
import placeholder.organisation.unicms.service.dto.ClassRoomTypeDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, componentModel = "spring")
public interface ClassRoomTypeMapper {
    ClassRoomTypeDTO toDto(ClassRoomType classRoom);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(ClassRoomTypeDTO dto, @MappingTarget ClassRoomType entity);
}
