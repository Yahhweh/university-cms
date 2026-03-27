package placeholder.organisation.unicms.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponseDTO {
    private String city;

    private String street;

    private String phoneNumber;

    private String country;

    private String houseNumber;

    private String postalCode;
}