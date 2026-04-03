package placeholder.organisation.unicms.service.dto.request.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonFilter implements RedirectAttributesProvider {
    Long durationId;
    String subject;
    String group;
    LecturerFilter lecturer = new LecturerFilter();
    String room;

    @Override
    public Map<String, String> toAttributes() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("durationId", emptyIfNull(durationId));
        attributes.put("subject", lowerOrEmpty(subject));
        attributes.put("lecturer.name", lowerOrEmpty(lecturer.name));
        attributes.put("lecturer.sureName", lowerOrEmpty(lecturer.sureName));
        attributes.put("room", lowerOrEmpty(room));
        return attributes;
    }
}
