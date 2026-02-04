package placeholder.organisation.unicms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Table(name = "class_room_type")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoomType {

    public ClassRoomType(String name, Long capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @Column(name = "name")
    @Size(min = 2, max = 35)
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name must contain only letters")
    String name;
    @Column(name = "capacity")
    Long capacity;
}
