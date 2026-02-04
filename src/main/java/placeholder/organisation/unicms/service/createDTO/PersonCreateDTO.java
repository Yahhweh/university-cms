package placeholder.organisation.unicms.service.createDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import placeholder.organisation.unicms.entity.GenderType;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonCreateDTO {
    private Long id;
    private String name;
    private String sureName;
    private GenderType gender;
    private String email;
    private AddressCreateDTO address;
    private LocalDate dateOfBirth;
}