package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import placeholder.organisation.unicms.entity.ClassRoomType;
import placeholder.organisation.unicms.service.createDTO.ClassRoomTypeDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface ClassRoomTypeMapper {
    ClassRoomTypeDTO toDto(ClassRoomType classRoom);
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(ClassRoomTypeDTO dto, @MappingTarget ClassRoomType entity);
}
