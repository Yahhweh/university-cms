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
    public Lesson(Duration duration, StudySubject studySubject, Group group, Lecturer lecturer, ClassRoom classRoom, LocalDate date) {
        this.duration = duration;
        this.studySubject = studySubject;
        this.group = group;
        this.lecturer = lecturer;
        this.classRoom = classRoom;
        this.date = date;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "duration_id", foreignKey = @ForeignKey(name = "fk_lesson_duration"))
    private Duration duration;
    @ManyToOne
    @JoinColumn(name = "study_subject_id", foreignKey = @ForeignKey(name = "fk_lesson_subject"))
    private StudySubject studySubject;
    @ManyToOne
    @JoinColumn(name = "group_id", foreignKey = @ForeignKey(name = "fk_lesson_group"))
    private Group group;
    @ManyToOne
    @JoinColumn(name = "lecturer_id", foreignKey = @ForeignKey(name = "fk_lesson_lecturer"))
    private Lecturer lecturer;
    @ManyToOne
    @JoinColumn(name = "class_room_id", foreignKey = @ForeignKey(name = "fk_lesson_classroom"))
    private ClassRoom classRoom;
    @Column
    private LocalDate date;
}
