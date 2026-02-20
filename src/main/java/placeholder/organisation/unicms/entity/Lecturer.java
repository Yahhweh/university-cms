package placeholder.organisation.unicms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "lecturer")
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class Lecturer extends Person {

    @Column(name = "salary")
    @Pattern(regexp = "^[0-9]+$")
    private Integer salary;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "lecturer_study_subject",
            joinColumns = @JoinColumn(name = "lecturer_id"),
            inverseJoinColumns = @JoinColumn(name = "study_subject_id"))
    private Set<StudySubject> studySubjects = new HashSet<>();

}
