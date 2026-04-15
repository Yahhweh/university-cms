package placeholder.organisation.unicms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "lecturer")
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("Lecturer")
public class Lecturer extends User {

    @Column(name = "salary")
    @Min(value = 0, message = "{salary.min}")
    @Max(value = 999999, message = "{salary.max}")
    private Integer salary;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "lecturer_study_subject",
        joinColumns = @JoinColumn(name = "lecturer_id"),
        inverseJoinColumns = @JoinColumn(name = "study_subject_id"))
    private Set<Subject> subjects = new HashSet<>();

    public String toString() {
        return this.getName() + " " + this.getSureName();
    }
}

