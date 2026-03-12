package placeholder.organisation.unicms.service.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class LecturerRequestDTO extends PersonRequestDTO {
    @NotNull
    @Min(0)
    private Integer salary;

    @NotNull
    @Size(min = 1)
    @Valid
    private Set<Long> studySubjectIds;
}
