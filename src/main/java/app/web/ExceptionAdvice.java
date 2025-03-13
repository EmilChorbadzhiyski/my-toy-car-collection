package app.web;


import app.exeptions.LoginFailedException;
import app.exeptions.PasswordValidationException;
import app.exeptions.UsernameAlreadyExistException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import javax.security.auth.login.LoginException;
import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(UsernameAlreadyExistException.class)
    public String handleUsernameAlreadyExist(RedirectAttributes redirectAttributes, UsernameAlreadyExistException exception) {

        String message = exception.getMessage();
        redirectAttributes.addFlashAttribute("usernameAlreadyExistMessage", message);

        return "redirect:/register";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            AccessDeniedException.class,
            NoResourceFoundException.class,
            MethodArgumentTypeMismatchException.class,
            MissingRequestValueException.class
    })
    public ModelAndView handleNotFoundExceptions() {
        return new ModelAndView("not-found");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAnyException(Exception exception) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("internal-server-error");
        modelAndView.addObject("errorMessage", exception.getClass().getSimpleName());

        return modelAndView;
    }

    @ExceptionHandler(PasswordValidationException.class)
    public String handlePasswordValidationException(RedirectAttributes redirectAttributes, PasswordValidationException exception) {
        String message = exception.getMessage();
        redirectAttributes.addFlashAttribute("passwordValidationMessage", message);
        return "redirect:/register";
    }

    @ExceptionHandler(LoginFailedException.class)
    public String handleLoginFailedException(RedirectAttributes redirectAttributes, LoginFailedException exception) {
        String message = exception.getMessage();
        redirectAttributes.addFlashAttribute("loginFailedMessage", message);
        return "redirect:/login";
    }
}
