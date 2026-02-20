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
@Builder
public class LessonDTO {
    @NotNull
    @Valid
    private Long durationId;

    @NotNull
    @Valid
    private Long studySubjectId;

    @NotNull
    @Valid
    private Long groupId;

    @NotNull
    @Valid
    private Long lecturerId;

    @NotNull
    @Valid
    private Long classRoomId;

    @NotNull
    private LocalDate date;

    public LessonDTO(LocalDate date) {
        this.date = date;
    }

}