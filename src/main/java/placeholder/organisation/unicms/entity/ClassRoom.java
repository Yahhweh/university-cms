package placeholder.organisation.unicms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Table(name = "class_room")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Size(min = 3, max = 35)
    @Pattern(regexp = "^[a-zA-Z]-[0-9]+$", message = "Name must contain on letters")
    String room;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_room_type_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_classroom_type"))
    ClassRoomType classRoomType;

}
