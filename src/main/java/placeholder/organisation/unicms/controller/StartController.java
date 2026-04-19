package placeholder.organisation.unicms.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import placeholder.organisation.unicms.entity.Role;
import placeholder.organisation.unicms.entity.User;
import placeholder.organisation.unicms.service.EntityNotFoundException;
import placeholder.organisation.unicms.service.UserService;

@Controller
@AllArgsConstructor
public class StartController {

    private final UserService userService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String show(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new EntityNotFoundException(User.class, userDetails.getUsername()));

        if(user.getRoles().contains(Role.LECTURER)){
            model.addAttribute("lecturer", user);
        }
        model.addAttribute("name");
        return "index";
    }
}
