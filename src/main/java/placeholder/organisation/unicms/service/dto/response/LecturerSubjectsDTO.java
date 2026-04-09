package placeholder.organisation.unicms.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class LecturerSubjectsDTO {
    private Long id;
    private String fullName;
    private Set<Long> subjectIds;
}
