package placeholder.organisation.unicms.service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomTypeDTO {
    @NotBlank
    @Size(min = 2, max = 35)
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name must contain only letters")
    private String name;

    @NotNull
    @Min(value = 1, message = "capacity from 1")
    @Max(value = 500)
    private Long capacity;

    public RoomTypeDTO(String name) {
        this.name = name;
    }

    public RoomTypeDTO(Long capacity) {
        this.capacity = capacity;
    }

}