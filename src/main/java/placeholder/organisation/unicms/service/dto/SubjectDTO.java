package placeholder.organisation.unicms.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDTO {
    @NotBlank
    @Size(min = 2, max = 100)
    @Pattern(regexp = "^[A-Z][a-zA-Z\\s]+$", message = "Subject name must start with uppercase letter")
    private String name;
}