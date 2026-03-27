package placeholder.organisation.unicms.service.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomTypeRequestDTO {
    @NotBlank
    @Size(min = 2, max = 35)
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name must contain only letters")
    private String name;

    @NotNull
    @Min(value = 1, message = "capacity from 1")
    @Max(value = 500)
    private Long capacity;

    public RoomTypeRequestDTO(String name) {
        this.name = name;
    }

    public RoomTypeRequestDTO(Long capacity) {
        this.capacity = capacity;
    }

}