package placeholder.organisation.unicms.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DurationRequestDTO {
    @NotNull
    private LocalTime start;

    @NotNull
    private LocalTime end;
}