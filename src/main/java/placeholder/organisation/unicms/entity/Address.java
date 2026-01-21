package placeholder.organisation.unicms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    String city;
    @Column(name = "street")
    String street;
    @Column(name = "phone_number")
    String phoneNumber;
    @Column(name = "country")
    String country;
    @Column(name = "house_number")
    String houseNumber;
    @Column(name = "postal_code")
    String postalCode;

}
