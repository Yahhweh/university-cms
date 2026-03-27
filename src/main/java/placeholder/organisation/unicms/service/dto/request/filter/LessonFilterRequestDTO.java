package placeholder.organisation.unicms.service.dto.request.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonFilterRequestDTO {
    Long durationId;
    String subject;
    String group;
    LecturerFilterRequestDTO lecturer = new LecturerFilterRequestDTO();
    String room;
}
