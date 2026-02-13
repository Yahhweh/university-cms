package placeholder.organisation.unicms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Table(name = "study_subject")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class StudySubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Pattern(regexp = "^[A-Z][a-z]+$")
    @Column(name = "name", nullable = false)
    String name;

    public StudySubject(String name) {
        this.name = name;
    }
}
