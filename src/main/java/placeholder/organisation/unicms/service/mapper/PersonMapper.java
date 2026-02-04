package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import placeholder.organisation.unicms.entity.Person;
import placeholder.organisation.unicms.service.createDTO.PersonDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface PersonMapper {
    PersonDTO toDto(Person person);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(PersonDTO dto, @MappingTarget Person entity);
}
