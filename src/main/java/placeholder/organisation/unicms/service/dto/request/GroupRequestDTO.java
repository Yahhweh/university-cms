package placeholder.organisation.unicms.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupRequestDTO {
    @NotBlank
    @Pattern(regexp = "^[A-Z]-[0-9]+$", message = "Group name must follow format: UppercaseLetter-Number (e.g., A-101)")
    private String name;
}