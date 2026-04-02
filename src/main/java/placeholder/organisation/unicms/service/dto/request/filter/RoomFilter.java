package placeholder.organisation.unicms.service.dto.request.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomFilter implements RedirectAttributesProvider {
    String number;
    String roomType;

    @Override
    public Map<String, String> toAttributes() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("number", number != null ? number.toLowerCase() : "");
        attributes.put("roomType", roomType != null ? roomType.toLowerCase() : "");
        return attributes;
    }
}
