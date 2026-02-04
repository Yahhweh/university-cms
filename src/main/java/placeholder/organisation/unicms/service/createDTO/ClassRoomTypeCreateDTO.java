package placeholder.organisation.unicms.service.createDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoomTypeCreateDTO {
    private Long id;

    @NotBlank
    @Size(min = 2, max = 35, message = "Name must be between 2 and 35 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name must contain only letters")
    private String name;

    @NotNull
    @Min(value = 1, message = "capacity from 1")
    @Max(value = 500)
    private Long capacity;
}