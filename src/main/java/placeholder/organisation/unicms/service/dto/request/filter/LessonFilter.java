package placeholder.organisation.unicms.service.dto.request.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonFilter {
    Long durationId;
    String subject;
    String group;
    LecturerFilter lecturer = new LecturerFilter();
    String room;
}
