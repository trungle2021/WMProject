package wm.clientmvc.controllers.Customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wm.clientmvc.DTO.CustomerDTO;
import wm.clientmvc.DTO.LoginDTO;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.SD_CLIENT;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @GetMapping("/login")
    public String customerLogin(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String roleCheck = authentication.getAuthorities().stream().findFirst().toString();
        if (!roleCheck.contains("ANONYMOUS")) {
            return "redirect:/";
        }
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";


    }

    @GetMapping("/all")
    public String getAll(Model model, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws IOException {
        ParameterizedTypeReference<List<CustomerDTO>> responseType = new ParameterizedTypeReference<List<CustomerDTO>>() {};

        try {
            List<CustomerDTO> customerDTOList = APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/customers/all",
                    HttpMethod.GET,
                    null,
                    token,
                    responseType);
            model.addAttribute("list", customerDTOList);
        } catch (HttpClientErrorException ex) {
            String responseError = ex.getResponseBodyAsString();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(responseError, Map.class);
            String message = map.get("message").toString();

            String status = String.valueOf(ex.getStatusCode().value());
            switch (status) {
                case "401":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/customer/login";
                case "404":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/access-denied";
                case "403":
                    return "redirect:/access-denied";
            }
        }
        return "test";
    }
}
