package placeholder.organisation.unicms.service.createDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class LecturerCreateDTO extends PersonCreateDTO {
    private Integer salary;
    private Set<StudySubjectCreateDTO> studySubjects;
}