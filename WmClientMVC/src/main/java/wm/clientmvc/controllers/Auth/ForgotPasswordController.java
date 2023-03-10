package wm.clientmvc.controllers.Auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wm.clientmvc.DTO.CustomerAccountDTO;
import wm.clientmvc.DTO.GenericResponse;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.SD_CLIENT;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/customer")
public class ForgotPasswordController {
    String findCustomerByEmailAPI = SD_CLIENT.DOMAIN_APP_API + "/api/auth/customer/processForgotPassword";
    @GetMapping("/forgot_password")
    public String showForgotPasswordForm(){
        return "forgot_password_form";
    }

    @GetMapping("/reset_password")
    public String showChangePasswordForm(){
        return "";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, Model model) throws IOException {
        String email = request.getParameter("email");
        if(!StringUtils.hasLength(email)){
            redirectAttributes.addFlashAttribute("error","Email must be required");
            return "redirect:/forgot_password";
        }
        try{
            String response1 = APIHelper.makeApiCall(
                    findCustomerByEmailAPI,
                    HttpMethod.POST,
                    email,
                    null,
                    String.class);
            redirectAttributes.addFlashAttribute("message","We have sent a reset password link to your email.Please check");
            return "redirect:/customer/forgot_password";

        }catch(HttpClientErrorException e){
            String responseError = e.getResponseBodyAsString();
            if(StringUtils.hasLength(responseError)){
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> map = mapper.readValue(responseError, Map.class);
                String error = map.get("error").toString();
                String status = String.valueOf(e.getStatusCode().value());
                switch (status) {
                    case "403":
                        return "redirect:/access-denied";
                    default:
                        redirectAttributes.addFlashAttribute("errorMessage", error);
                        return "redirect:customer/forgot_password" ;
                }
            }
        }
        return "redirect:/error";
    }

}
