package placeholder.organisation.unicms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "student")
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("Student")
@PrimaryKeyJoinColumn(name = "id")
public class Student extends Person {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = true,
            referencedColumnName = "id")
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(name = "degree_type_attr")
    private Degree degree;
}
