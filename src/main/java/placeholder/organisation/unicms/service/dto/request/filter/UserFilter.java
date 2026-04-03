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
        attributes.put("name", lowerOrEmpty(name));
        attributes.put("sureName", lowerOrEmpty(sureName));
        attributes.put("email", lowerOrEmpty(email));
        attributes.put("role", emptyIfNull(role));
        return attributes;
    }
}
