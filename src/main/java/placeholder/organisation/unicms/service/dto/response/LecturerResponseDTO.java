package placeholder.organisation.unicms.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class LecturerResponseDTO extends UserResponseDTO {
    private Integer salary;

    private Set<Long> studySubjectIds;
}