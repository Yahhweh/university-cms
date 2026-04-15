package placeholder.organisation.unicms.service.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class LecturerRequestDTO extends UserRequestDTO {
    @NotNull
    @Min(0)
    private Integer salary;

    @NotNull
    @Size(min = 1)
    @Valid
    private Set<Long> studySubjectIds;
}
