package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.Person;
import placeholder.organisation.unicms.service.dto.response.PersonResponseDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PersonMapper {
    PersonResponseDTO toDto(Person person);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(PersonResponseDTO dto, @MappingTarget Person entity);
}
