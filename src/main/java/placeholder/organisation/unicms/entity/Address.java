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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "city")
    String city;
    @Column(name = "street")
    String street;
    @Column(name = "country")
    String country;
    @Column(name = "house_number")
    String houseNumber;
    @Column(name = "postal_code")
    String postalCode;

}
