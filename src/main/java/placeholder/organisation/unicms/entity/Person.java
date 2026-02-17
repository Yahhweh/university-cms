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
public abstract class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "password")
    @Size(min = 8, max = 50, message = "{person.password.size}")
    private String password;

    @Column(name = "name")
    @Pattern(regexp = "[A-Z][a-z]+$", message = "{person.name.pattern}")
    private String name;

    @Column(name = "sure_name")
    @Pattern(regexp = "[A-Z][a-z]+$", message = "{person.surname.pattern}")
    private String sureName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderType gender;

    @Column(name = "email")
    @Pattern(regexp = "^[a-z]+\\.[a-z]+\\d*@[a-z]+\\.university\\.com$", message = "{person.email.pattern}")
    private String email;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id",
            foreignKey = @ForeignKey(name = "fk_person_address"),
            referencedColumnName = "id")
    private Address address;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

}