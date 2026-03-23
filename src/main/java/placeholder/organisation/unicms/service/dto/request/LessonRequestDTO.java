package placeholder.organisation.unicms.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonRequestDTO {
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

    public LessonRequestDTO(LocalDate date) {
        this.date = date;
    }

}