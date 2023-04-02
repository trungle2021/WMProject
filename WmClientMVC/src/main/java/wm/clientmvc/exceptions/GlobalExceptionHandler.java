package wm.clientmvc.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UnauthorizedException.class)
    public String handleUnauthorizedException(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String path = request.getServletPath();
        if(path.startsWith("/customers")){
            redirectAttributes.addFlashAttribute("errorMessage","Session Expired");
            return "redirect:/login";
        }else{
            redirectAttributes.addFlashAttribute("errorMessage","Session Expired");
            return "redirect:/staff/login";
        }
    }
}
