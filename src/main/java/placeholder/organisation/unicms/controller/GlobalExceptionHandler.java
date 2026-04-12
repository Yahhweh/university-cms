package placeholder.organisation.unicms.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import placeholder.organisation.unicms.service.EntityNotFoundException;
import placeholder.organisation.unicms.service.InsufficientRoleException;

import java.net.BindException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
        UsernameNotFoundException.class,
        BindException.class,
        BadCredentialsException.class,
        EntityNotFoundException.class,
        DataIntegrityViolationException.class,
        InsufficientRoleException.class
    })
    public String handleException(Exception ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:" + request.getRequestURI();
    }
}