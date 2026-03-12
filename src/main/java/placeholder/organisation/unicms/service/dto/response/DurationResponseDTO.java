package placeholder.organisation.unicms.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DurationResponseDTO {
    @NotNull
    private LocalTime start;

    @NotNull
    private LocalTime end;
}