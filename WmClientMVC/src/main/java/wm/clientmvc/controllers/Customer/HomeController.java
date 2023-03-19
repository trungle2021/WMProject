package wm.clientmvc.controllers.Customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wm.clientmvc.DTO.CustomerDTO;
import wm.clientmvc.DTO.JWTAuthResponse;
import wm.clientmvc.DTO.LoginDTO;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.SD_CLIENT;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @GetMapping(value = {"/home","/customers/home","/"})
    public String home() {
        return "home";
    }
    @GetMapping(value = {"/customers/dashboard",})
    public String dashboard() {
        return "/customerTemplate/dashboard";
    }

    @GetMapping(value = {"/about","/customers/about"})
    public String about() {
        return "about";
    }

    @GetMapping(value = {"/login"})
    public String customerLogin(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream().findFirst().toString();

        boolean userIsStaff = role.contains("ADMIN") || role.contains("SALE") || role.contains("ORGANIZE");
        boolean userIsCustomer = role.contains("CUSTOMER");
        boolean isAnonymous = role.contains("ANONYMOUS");
        if (userIsCustomer) {
            return "redirect:/customers/home";
        }
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }


}
