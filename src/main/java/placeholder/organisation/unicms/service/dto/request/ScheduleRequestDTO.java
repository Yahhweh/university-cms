package placeholder.organisation.unicms.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import placeholder.organisation.unicms.entity.DayOfWeek;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRequestDTO {
    @NotNull
    private DayOfWeek day;
    @NotNull
    private Long groupId;
    @NotNull
    private Long lecturerId;
    @NotNull
    private Long SubjectId;

}
