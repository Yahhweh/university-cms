package placeholder.organisation.unicms.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import placeholder.organisation.unicms.entity.Degree;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO extends PersonDTO {
    @Valid
    @NotNull
    private Long groupId;

    @NotNull
    private Degree degree;
}