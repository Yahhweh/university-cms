package placeholder.organisation.unicms.service.dto.response;

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
public class StudentResponseDTO extends PersonResponseDTO {
    @Valid
    @NotNull
    private Long groupId;

    @NotNull
    private Degree degree;
}