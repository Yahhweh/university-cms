package placeholder.organisation.unicms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
    @Size(min = 8, max = 50, message = "Wrong password size")
    private String password;

    @Column(name = "name")
    @Pattern(regexp = "[A-Z][a-z]+$", message = "Name must starts with uppercase letter and must contain only letters")
    private String name;

    @Column(name = "sure_name")
    @Pattern(regexp = "[A-Z][a-z]+$", message = "Sure name must starts with uppercase letter and must contain only letters")
    private String sureName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderType gender;
    @Column(name = "email")
    @Pattern(regexp = "^[a-z]+\\.[a-z]+\\d*@student\\.university\\.com$", message = "Email must follow this type name.surename@student.university.com")
    private String email;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id",
            foreignKey = @ForeignKey(name = "fk_person_address"),
            referencedColumnName = "id")
    private Address address;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
}
