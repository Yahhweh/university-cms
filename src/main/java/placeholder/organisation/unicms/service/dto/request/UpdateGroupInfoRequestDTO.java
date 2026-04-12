package placeholder.organisation.unicms.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateGroupInfoRequestDTO {
    private Long groupId;

    @NotBlank
    private String info;
}
