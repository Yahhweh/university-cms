package placeholder.organisation.unicms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Log4j2
@Entity
@Data
@Table(name = "person")
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person implements UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    @Pattern(regexp = "[A-Z][a-z]+$", message = "{person.name.pattern}")
    private String name;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "sure_name")
    @Pattern(regexp = "[A-Z][a-z]+$", message = "{person.surname.pattern}")
    private String sureName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderType gender;

    @Column(name = "email")
    @Pattern(regexp = "^[a-z]+\\.[a-z]+\\d*@[a-z]+\\.university\\.com$", message = "{person.email.pattern}")
    private String email;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id",
            foreignKey = @ForeignKey(name = "fk_person_address"),
            referencedColumnName = "id")
    private Address address;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Override
    public String getUsername(){
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String toString() {
        return "Person{id=" + id + ", name=" + name + ", sureName=" + sureName + ", role=" + role + "}";
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}