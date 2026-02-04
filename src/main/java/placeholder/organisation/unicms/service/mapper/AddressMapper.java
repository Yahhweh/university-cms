package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import placeholder.organisation.unicms.entity.Address;
import placeholder.organisation.unicms.service.createDTO.AddressDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface AddressMapper {
    AddressDTO toDto(Address address);
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AddressDTO dto, @MappingTarget Address entity);
}
