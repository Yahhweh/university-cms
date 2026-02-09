package placeholder.organisation.unicms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    private StudySubject studySubject;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;
    @ManyToOne
    @JoinColumn(name = "class_room_id")
    private ClassRoom classRoom;
    @Column
    private LocalDate date;

    public Lesson(Duration duration, StudySubject studySubject, Group group, Lecturer lecturer, ClassRoom classRoom, LocalDate date) {
        this.duration = duration;
        this.studySubject = studySubject;
        this.group = group;
        this.lecturer = lecturer;
        this.classRoom = classRoom;
        this.date = date;
    }
}
