package placeholder.organisation.unicms.service.dto.request.filter;

import java.util.Map;

public interface RedirectAttributesProvider {
    Map<String, String> toAttributes();

    default String emptyIfNull(Object value) {
        return value != null ? value.toString() : "";
    }

    default String lowerOrEmpty(String value) {
        return value != null ? value.toLowerCase() : "";
    }
}
