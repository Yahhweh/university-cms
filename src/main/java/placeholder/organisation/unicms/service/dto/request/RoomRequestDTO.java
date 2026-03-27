package placeholder.organisation.unicms.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequestDTO {
    @NotBlank()
    @Size(min = 3, max = 35)
    @Pattern(regexp = "^[a-zA-Z]-[0-9]+$", message = "Room must follow format: Letter-Number (e.g., A-101)")
    private String room;

    @NotNull()
    @Valid
    private Long classRoomTypeId;

    public RoomRequestDTO(String room) {
        this.room = room;
    }

    public RoomRequestDTO(long classRoomTypeId) {
        this.classRoomTypeId = classRoomTypeId;
    }

}
