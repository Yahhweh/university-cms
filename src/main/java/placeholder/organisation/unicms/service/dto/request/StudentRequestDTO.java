package placeholder.organisation.unicms.service.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import placeholder.organisation.unicms.entity.Degree;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudentRequestDTO extends UserRequestDTO {
    @Valid
    private Long groupId;

    @NotNull
    private Degree degree;
}
