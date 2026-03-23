package placeholder.organisation.unicms.service.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import placeholder.organisation.unicms.entity.Role;

import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequestDTO {
    String name;
    String sureName;
    String email;
    @Pattern(regexp = "^[A-zA-z]")
    Role role;
}
