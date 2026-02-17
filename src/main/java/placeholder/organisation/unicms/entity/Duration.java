package placeholder.organisation.unicms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.time.LocalTime;

@Table(name = "duration")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Duration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start")
    LocalTime start;

    @Column(name = "\"end\"")
    LocalTime end;
}
