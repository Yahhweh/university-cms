package placeholder.organisation.unicms.service.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import placeholder.organisation.unicms.entity.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequestDTO {
    Long id;
    String name;
    String sureName;
    String email;
    Role role;
}
