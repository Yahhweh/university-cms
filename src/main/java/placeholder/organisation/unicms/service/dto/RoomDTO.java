package placeholder.organisation.unicms.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {
    @NotBlank()
    @Size(min = 3, max = 35)
    @Pattern(regexp = "^[a-zA-Z]-[0-9]+$", message = "Room must follow format: Letter-Number (e.g., A-101)")
    private String room;

    @NotNull()
    @Valid
    private Long classRoomTypeId;

    public RoomDTO(String room) {
        this.room = room;
    }

    public RoomDTO(long classRoomTypeId) {
        this.classRoomTypeId = classRoomTypeId;
    }

}
