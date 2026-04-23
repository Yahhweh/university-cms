package placeholder.organisation.unicms.entity;

import jakarta.persistence.*;
import lombok.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalTime;

@Table(name = "duration")
@Entity
@Getter
@Setter
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

    public String toString() {
        return this.getStart() + "-" + this.getEnd();
    }
}
