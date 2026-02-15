package placeholder.organisation.unicms.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonDTO {
    @NotNull
    @Valid
    private DurationDTO duration;

    @NotNull
    @Valid
    private StudySubjectDTO studySubject;

    @NotNull
    @Valid
    private GroupDTO group;

    @NotNull
    @Valid
    private LecturerDTO lecturer;

    @NotNull
    @Valid
    private ClassRoomDTO classRoom;

    @NotNull
    private LocalDate date;

    public LessonDTO(DurationDTO duration) {
        this.duration = duration;
    }

    public LessonDTO(StudySubjectDTO studySubject) {
        this.studySubject = studySubject;
    }

    public LessonDTO(GroupDTO group) {
        this.group = group;
    }

    public LessonDTO(LecturerDTO lecturer) {
        this.lecturer = lecturer;
    }

    public LessonDTO(ClassRoomDTO classRoom) {
        this.classRoom = classRoom;
    }

    public LessonDTO(LocalDate date) {
        this.date = date;
    }

}