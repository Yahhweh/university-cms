package placeholder.organisation.unicms.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

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