package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.Person;
import placeholder.organisation.unicms.service.dto.PersonDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PersonMapper {
    PersonDTO toDto(Person person);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(PersonDTO dto, @MappingTarget Person entity);
}
