package placeholder.organisation.unicms.service.createDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoomCreateDTO {
    private Long id;

    @NotBlank()
    @Size(min = 3, max = 35, message = "Room must be between 3 and 35 characters")
    @Pattern(regexp = "^[a-zA-Z]-[0-9]+$", message = "Room must follow format: Letter-Number (e.g., A-101)")
    private String room;

    @NotNull()
    @Valid
    private ClassRoomTypeCreateDTO classRoomType;
}
