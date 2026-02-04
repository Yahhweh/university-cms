package placeholder.organisation.unicms.service.createDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressCreateDTO {
    private Long id;

    @NotBlank
    @Size(min = 2, max = 100, message = "City must be between 2 and 100 characters")
    private String city;

    @NotBlank
    @Size(min = 2, max = 100, message = "Street must be between 2 and 100 characters")
    private String street;

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be valid (10-15 digits)")
    private String phoneNumber;

    @NotBlank
    @Size(min = 2, max = 100, message = "Country must be between 2 and 100 characters")
    private String country;

    @NotBlank
    @Size(max = 20, message = "House number must not exceed 20 characters")
    private String houseNumber;

    @NotBlank()
    @Pattern(regexp = "^[A-Z0-9]{3,10}$", message = "Postal code must be 3-10 alphanumeric characters")
    private String postalCode;
}