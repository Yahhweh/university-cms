package placeholder.organisation.unicms.service.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateLessonsRequestDTO {
    @NotNull
    private Long scheduleId;
    @NotNull
    private Long classRoomId;
    @NotNull
    @Min(1)
    @Max(4)
    private Integer weeks;
}
