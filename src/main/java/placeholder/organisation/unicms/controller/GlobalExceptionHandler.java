package placeholder.organisation.unicms.controller;

import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import placeholder.organisation.unicms.service.EntityNotFoundException;

import java.net.BindException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(UsernameNotFoundException ex, Model model){
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("error", 401);
        model.addAttribute("title", "User not Found");
        return "error";
    }

    @ExceptionHandler(BindException.class)
    public String handleBindException(BindException exception, Model model){
        model.addAttribute("message", exception.getMessage());
        model.addAttribute("error", 400);
        model.addAttribute("title", "Wrong Validation");
        return "error";
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String handleBadCredentialsException(BadCredentialsException ex, Model model){
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("error", 401);
        model.addAttribute("title", "Bad Credentials");
        return "error";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundException(EntityNotFoundException ex, Model model){
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("error", 404);
        model.addAttribute("title", "Entity not found");
        return "redirect:error";
    }
}