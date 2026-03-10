package placeholder.organisation.unicms.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalThemeAdvice {
    @Value("${app.ui.theme:cerulean}")
    private String theme;

    @ModelAttribute("theme")
    public String getTheme(){
        return theme;
    }
}
