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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wm.clientmvc.DTO.PasswordDTO;
import wm.clientmvc.utils.APIHelper;

import java.io.IOException;
import java.util.Map;

import static wm.clientmvc.utils.SD_CLIENT.*;

@Controller
public class ForgotPasswordController {


    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, Model model) throws IOException {
        String email = request.getParameter("email");
        if (!StringUtils.hasLength(email)) {
            redirectAttributes.addFlashAttribute("error", "Email must be required");
            return "redirect:/forgot_password";
        }
        try {
            String response1 = APIHelper.makeApiCall(
                    api_processForgotPassword,
                    HttpMethod.POST,
                    email,
                    null,
                    String.class,request,response);
            redirectAttributes.addFlashAttribute("message", "We have sent a password reset link to your email. Please check your inbox and follow the instructions provided.");
            return "redirect:/forgot_password";

        } catch (HttpClientErrorException e) {
            String responseError = e.getResponseBodyAsString();
            if (StringUtils.hasLength(responseError)) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> map = mapper.readValue(responseError, Map.class);
                String error = map.get("message").toString();
                String status = String.valueOf(e.getStatusCode().value());
                switch (status) {
                    case "403":
                        return "redirect:/access-denied";
                    default:
                        redirectAttributes.addFlashAttribute("errorMessage", error);
                        return "redirect:/forgot_password";
                }
            }
        }
        return "redirect:/error";
    }

    @GetMapping("/changePassword")
    public String showChangePasswordForm(@RequestParam(value = "token", defaultValue = "") String token, RedirectAttributes redirectAttributes, Model model) {
        if (!StringUtils.hasLength(token)) {
            redirectAttributes.addFlashAttribute("error", "Invalid Token");
            return "redirect:/forgot_password";
        }
        model.addAttribute("reset_token", token);
        return "changePasswordForm";
    }

    @PostMapping("/changePassword")
    public String changePassword( RedirectAttributes redirectAttributes, Model model, HttpServletResponse response,HttpServletRequest request) throws IOException {
        String password = request.getParameter("password") != null ? request.getParameter("password") : "";
        String cpassword = request.getParameter("cpassword") != null ? request.getParameter("cpassword") : "";
        String token = request.getParameter("reset_token") != null ? request.getParameter("reset_token") : "";
        if (!password.equals(cpassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Confirmed Password and New Password do not match!");
            return "redirect:/changePassword?token=" + token;
        }
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setNewPassword(password);
        passwordDTO.setConfirmPassword(cpassword);
        passwordDTO.setToken(token);
        try {
            String _response = APIHelper.makeApiCall(
                    api_processChangePassword,
                    HttpMethod.POST,
                    passwordDTO,
                    null,
                    String.class,request,response);
            redirectAttributes.addFlashAttribute("message", _response);
            return "redirect:/login";

        } catch (HttpClientErrorException e) {
            String responseError = e.getResponseBodyAsString();
            if (StringUtils.hasLength(responseError)) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> map = mapper.readValue(responseError, Map.class);
                String error = map.get("message").toString();
                String status = String.valueOf(e.getStatusCode().value());
                switch (status) {
                    case "403":
                        return "redirect:/access-denied";
                    default:
                        redirectAttributes.addFlashAttribute("errorMessage", error);
                        return "redirect:/forgot_password";
                }
            }
        }
        return "redirect:/error";
    }


}
