package placeholder.organisation.unicms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "person")
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(
        name = "type",
        discriminatorType = DiscriminatorType.STRING,
        length = 20
)
public abstract class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "sure_name")
    private String sureName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderType gender;
    @Column(name = "email")
    private String email;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id",
            foreignKey = @ForeignKey(name = "fk_person_address"),
            referencedColumnName = "id")
    private Address address;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
}
