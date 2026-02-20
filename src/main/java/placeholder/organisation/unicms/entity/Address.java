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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "city")
    @NotBlank(message = "{address.city.notblank}")
    @Size(min = 2, max = 100, message = "{address.city.size}")
    String city;

    @NotBlank(message = "{address.street.notblank}")
    @Size(min = 2, max = 100, message = "{address.street.size}")
    @Column(name = "street")
    String street;

    @NotBlank(message = "{address.phone.notblank}")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "{address.phone.pattern}")
    @Column(name = "phone_number")
    String phoneNumber;

    @NotBlank(message = "{address.country.notblank}")
    @Size(min = 2, max = 100, message = "{address.country.size}")
    @Column(name = "country")
    String country;

    @Column(name = "house_number")
    @NotBlank(message = "{address.house.notblank}")
    @Size(max = 20, message = "{address.house.size}")
    String houseNumber;

    @NotBlank(message = "{address.postalCode.notblank}")
    @Pattern(regexp = "^[A-Z0-9]{3,10}$", message = "{address.postalCode.pattern}")
    @Column(name = "postal_code")
    String postalCode;

    public Address(String city, String street, String country, String houseNumber, String postalCode, String phoneNumber) {
        this.city = city;
        this.street = street;
        this.country = country;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
    }

    public String toString(){
        return this.getCountry();
    }
}