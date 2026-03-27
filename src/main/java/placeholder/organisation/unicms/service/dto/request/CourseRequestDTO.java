package placeholder.organisation.unicms.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CourseRequestDTO {
    @NotBlank
    @Size(min = 3, max = 30)
    String name;

    List<Long> subjectIds;
}
