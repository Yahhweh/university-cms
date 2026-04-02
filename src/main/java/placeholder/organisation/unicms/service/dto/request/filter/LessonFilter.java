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
        attributes.put("durationId", durationId != null ? durationId.toString() : "");
        attributes.put("subject", subject != null ? subject.toLowerCase() : "");
        attributes.put("lecturer.name", lecturer.name != null ? lecturer.name.toLowerCase() : "");
        attributes.put("lecturer.sureName", lecturer.sureName != null ? lecturer.sureName.toLowerCase() : "");
        attributes.put("room", room != null ? room.toLowerCase() : "");
        return attributes;
    }
}
