package placeholder.organisation.unicms.service.dto.request.filter;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import placeholder.organisation.unicms.entity.Role;

import jakarta.validation.constraints.*;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFilter implements RedirectAttributesProvider {
    String name;
    String sureName;
    String email;
    @Pattern(regexp = "^[A-zA-z]")
    Role role;
    @Pattern(regexp = "^[A-zA-z]")
    String dType;

    @Override
    public Map<String, String> toAttributes() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("name", name != null ? name.toLowerCase() : "");
        attributes.put("sureName", sureName != null ? sureName.toLowerCase() : "");
        attributes.put("email", email != null ? email.toLowerCase() : "");
        attributes.put("role", role != null ? role.toString() : "");
        return attributes;
    }
}
