package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.User;
import placeholder.organisation.unicms.service.dto.request.UserRequestDTO;
import placeholder.organisation.unicms.service.dto.response.UserResponseDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring",
    uses = {AddressMapper.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserResponseDTO toDto(User user);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(UserResponseDTO dto, @MappingTarget User entity);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequestDTO dto);
}
