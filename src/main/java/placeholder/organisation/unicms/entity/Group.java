package placeholder.organisation.unicms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Data
@Table(name = "\"group\"")
@AllArgsConstructor
@NoArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Pattern(regexp = "^[A-Z]{2}-[0-9]+$", message = "{group.name.pattern}")
    @Column(name = "name")
    String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course")
    Course course;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ColumnDefault("1")
    @JoinColumn(name = "mentor_id", nullable = false)
    private User mentor;

    @NotNull
    @ColumnDefault("'Group info'")
    @Column(name = "info", nullable = false, length = Integer.MAX_VALUE)
    private String info;
    public String toString(){
        return this.getName();
    }
}
