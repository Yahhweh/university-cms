package placeholder.organisation.unicms.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(name = "lesson")
public class Lesson {
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
