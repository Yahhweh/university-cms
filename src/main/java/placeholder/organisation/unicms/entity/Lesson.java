package placeholder.organisation.unicms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "lesson")
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "duration_id")
    private Duration duration;

    @ManyToOne
    @JoinColumn(name = "study_subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;

    @ManyToOne
    @JoinColumn(name = "class_room_id")
    private Room room;

    @Column
    private LocalDate date;

    public Lesson(Duration duration, Subject subject, Group group, Lecturer lecturer, Room room, LocalDate date) {
        this.duration = duration;
        this.subject = subject;
        this.group = group;
        this.lecturer = lecturer;
        this.room = room;
        this.date = date;
    }
}
