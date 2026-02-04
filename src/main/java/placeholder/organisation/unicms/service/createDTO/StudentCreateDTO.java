package placeholder.organisation.unicms.service.createDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import placeholder.organisation.unicms.entity.Degree;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class StudentCreateDTO extends PersonCreateDTO {
    private GroupCreateDTO group;
    private Degree degree;
}