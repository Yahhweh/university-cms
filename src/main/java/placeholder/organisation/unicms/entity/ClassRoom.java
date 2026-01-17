package placeholder.organisation.unicms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "class_room")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String room;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_room_type_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_classroom_type"))
    ClassRoomType classRoomType;

}
