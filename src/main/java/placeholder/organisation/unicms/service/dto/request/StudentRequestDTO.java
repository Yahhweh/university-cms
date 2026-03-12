package placeholder.organisation.unicms.service.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import placeholder.organisation.unicms.entity.Degree;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudentRequestDTO extends PersonRequestDTO{
    @Valid
    @NotNull
    private Long groupId;

    @NotNull
    private Degree degree;
}
