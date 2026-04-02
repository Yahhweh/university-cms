package placeholder.organisation.unicms.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import placeholder.organisation.unicms.service.dto.request.filter.RedirectAttributesProvider;

public class PageProvider {

    public static void providePages(String message, Pageable pageable, RedirectAttributes redirectAttributes, RedirectAttributesProvider filter){
        redirectAttributes.addFlashAttribute("successMessage", message);
        redirectAttributes.addAttribute("page", pageable.getPageNumber());
        pageable.getSort().forEach(order ->
            redirectAttributes.addAttribute("sort",
                order.getProperty() + "," + order.getDirection().name().toLowerCase())
        );

        filter.toAttributes().forEach(redirectAttributes::addAttribute);
    }
}
