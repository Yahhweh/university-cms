package placeholder.organisation.unicms.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectRequestDTO {
    @NotBlank
    @Size(min = 2, max = 100)
    @Pattern(regexp = "^[A-Z][a-zA-Z\\s]+$", message = "Subject name must start with uppercase letter")
    private String name;
}