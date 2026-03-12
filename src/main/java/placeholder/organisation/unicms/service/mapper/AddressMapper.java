package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.Address;
import placeholder.organisation.unicms.service.dto.request.AddressRequestDTO;
import placeholder.organisation.unicms.service.dto.response.AddressResponseDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring")
public interface AddressMapper {
    AddressResponseDTO toDto(Address address);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AddressResponseDTO dto, @MappingTarget Address entity);

    Address toEntity(AddressResponseDTO addressResponseDTO);

    @Mapping(target = "id", ignore = true)
    Address toEntity(AddressRequestDTO addressRequestDTO);
}
