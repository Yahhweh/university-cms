package placeholder.organisation.unicms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Log4j2
@Entity
@Data
@Table(name = "\"user\"")
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("User")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    @Pattern(regexp = "^[A-Za-z]+(?:'[A-Za-z]+)*$", message = "{person.name.pattern}")
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @Column(name = "sure_name")
    @Pattern(regexp = "^[A-Za-z]+(?:'[A-Za-z]+)*$", message = "{person.surname.pattern}")
    private String sureName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderType gender;

    @Column(name = "email")
    @Pattern(regexp = "^[a-z0-9.']+@([a-z]+\\.)+[a-z]+$", message = "{person.email.pattern}")
    private String email;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id",
        foreignKey = @ForeignKey(name = "fk_person_address"),
        referencedColumnName = "id")
    private Address address;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(insertable = false, updatable = false)
    private String dtype;

    @Override
    public String toString() {
        return "User{id=" + id + ", name=" + name + ", sureName=" + sureName + ", roles=" + roles + "}";
    }
}