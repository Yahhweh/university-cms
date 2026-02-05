package placeholder.organisation.unicms.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class LecturerDTO extends PersonDTO {

    @NotNull
    @Min(0)
    private Integer salary;

    @NotNull
    @Size(min = 1)
    @Valid
    private Set<StudySubjectDTO> studySubjects;
}