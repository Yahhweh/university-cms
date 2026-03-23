package placeholder.organisation.unicms.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import placeholder.organisation.unicms.entity.Degree;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponseDTO extends UserResponseDTO {
    private Long groupId;

    private Degree degree;
}