package placeholder.organisation.unicms.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class StudentGroupDTO {
    private Long id;
    private String fullName;
    private Long groupIds;
}
