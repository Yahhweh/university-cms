package placeholder.organisation.unicms.service.dto.request.filter;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import placeholder.organisation.unicms.entity.Role;

import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFilter {
    String name;
    String sureName;
    String email;
    @Pattern(regexp = "^[A-zA-z]")
    Role role;
    @Pattern(regexp = "^[A-zA-z]")
    String dType;
}
