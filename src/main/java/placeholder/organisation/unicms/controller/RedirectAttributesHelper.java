package placeholder.organisation.unicms.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import placeholder.organisation.unicms.service.dto.request.filter.RedirectAttributesProvider;

public class RedirectAttributesHelper {

    public static void addPageAndFilterAttributes(String message, Pageable pageable,
                                                  RedirectAttributes redirectAttributes, RedirectAttributesProvider filter) {
        addPageAndFilterAttributes(message, pageable, redirectAttributes);
        filter.toAttributes().forEach(redirectAttributes::addAttribute);
    }

    public static void addPageAndFilterAttributes(String message, Pageable pageable, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("successMessage", message);
        redirectAttributes.addAttribute("page", pageable.getPageNumber());
        pageable.getSort().forEach(order ->
            redirectAttributes.addAttribute("sort",
                order.getProperty() + "," + order.getDirection().name().toLowerCase())
        );
    }
}
