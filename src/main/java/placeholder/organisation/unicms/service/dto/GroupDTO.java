package placeholder.organisation.unicms.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO {
    @NotBlank
    @Pattern(regexp = "^[A-Z]-[0-9]+$", message = "Group name must follow format: UppercaseLetter-Number (e.g., A-101)")
    private String name;
}