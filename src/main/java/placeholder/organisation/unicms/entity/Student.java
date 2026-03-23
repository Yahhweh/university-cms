package placeholder.organisation.unicms.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "student")
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("Student")
public class Student extends User {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = true,
            referencedColumnName = "id")
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(name = "degree")
    private Degree degree;
}