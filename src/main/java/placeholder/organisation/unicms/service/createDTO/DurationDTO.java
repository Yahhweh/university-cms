package placeholder.organisation.unicms.service.createDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DurationDTO {
    @Null
    private Long id;

    @NotNull
    private LocalTime start;

    @NotNull
    private LocalTime end;
}