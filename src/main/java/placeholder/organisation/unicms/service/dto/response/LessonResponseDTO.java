package placeholder.organisation.unicms.service.dto.response;

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
public class LessonResponseDTO {
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

    public LessonResponseDTO(LocalDate date) {
        this.date = date;
    }

}