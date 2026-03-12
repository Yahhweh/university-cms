package placeholder.organisation.unicms.service.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import placeholder.organisation.unicms.entity.GenderType;
import placeholder.organisation.unicms.service.dto.request.AddressRequestDTO;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonRequestDTO {
    @Null
    private Long id;

    @NotBlank
    @Pattern(regexp = "^[A-Z][a-z]+$", message = "Name must start with uppercase letter and contain only letters")
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank
    @Pattern(regexp = "^[A-Z][a-z]+$", message = "Sure name must start with uppercase letter and contain only letters")
    @Size(min = 2, max = 50)
    private String sureName;

    @NotNull
    private GenderType gender;

    @NotNull
    private String password;

    @Valid
    private AddressRequestDTO address;

    @NotNull
    @Past
    private LocalDate dateOfBirth;
}
