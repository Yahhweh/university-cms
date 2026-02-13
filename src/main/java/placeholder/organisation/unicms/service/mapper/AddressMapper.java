package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.Address;
import placeholder.organisation.unicms.service.dto.AddressDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring")
public interface AddressMapper {
    AddressDTO toDto(Address address);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AddressDTO dto, @MappingTarget Address entity);
}
