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
    private Long durationId;
    private String subject;
    private String group;
    private LecturerFilter lecturer = new LecturerFilter();
    private String room;

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
