package placeholder.organisation.unicms.service.createDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import placeholder.organisation.unicms.entity.GenderType;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {
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

    @NotBlank
    @Email
    @Pattern(regexp = "^[a-z]+\\.[a-z]+\\d*@student\\.university\\.com$",
            message = "Email must follow format: name.surname@student.university.com")
    private String email;

    @Valid
    private AddressDTO address;

    @NotNull
    @Past
    private LocalDate dateOfBirth;
}