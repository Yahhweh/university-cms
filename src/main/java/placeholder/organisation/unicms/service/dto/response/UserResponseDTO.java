package placeholder.organisation.unicms.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import placeholder.organisation.unicms.entity.GenderType;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String name;

    private String sureName;

    private GenderType gender;

    private String email;

    private AddressResponseDTO address;

    private LocalDate dateOfBirth;
}