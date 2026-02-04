package placeholder.organisation.unicms.service.createDTO;

import lombok.AllArgsConstructor;
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
    @Null
    private Long id;

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
}