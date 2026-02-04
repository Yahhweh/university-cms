package placeholder.organisation.unicms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "address")
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    public Address(String city, String street, String country, String houseNumber, String postalCode, String phoneNumber) {
        this.city = city;
        this.street = street;
        this.country = country;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "city")
    @NotBlank(message = "City is required")
    @Size(min = 2, max = 100, message = "City must be between 2 and 100 characters")
    String city;

    @NotBlank(message = "Street is required")
    @Size(min = 2, max = 100, message = "Street must be between 2 and 100 characters")
    @Column(name = "street")
    String street;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be valid (10-15 digits)")
    @Column(name = "phone_number")
    String phoneNumber;

    @NotBlank(message = "Country is required")
    @Size(min = 2, max = 100, message = "Country must be between 2 and 100 characters")
    @Column(name = "country")
    String country;

    @Column(name = "house_number")
    @NotBlank(message = "House number is required")
    @Size(max = 20, message = "House number must not exceed 20 characters")
    String houseNumber;

    @NotBlank(message = "Postal code is required")
    @Pattern(regexp = "^[A-Z0-9]{3,10}$", message = "Postal code must be 3-10 alphanumeric characters")
    @Column(name = "postal_code")
    String postalCode;

}
