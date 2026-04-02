package placeholder.organisation.unicms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( "")
public class AuthenticationController {

    @GetMapping("/login")
    String getLogin(Model model) {
        return "login";
    }

    @GetMapping("/access-denied")
    String getAccessDenied() {
        return "errors/access-denied";
    }
}
