package wm.clientmvc.controllers.Admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import wm.clientmvc.DTO.LoginDTO;
import wm.clientmvc.DTO.RevenueYearDTO;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.SD_CLIENT;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static wm.clientmvc.utils.SD_CLIENT.api_getRevenueByYear;
import static wm.clientmvc.utils.SD_CLIENT.api_teams_delete;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @GetMapping(value = {"/login"})
    public String employeeLogin(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream().findFirst().toString();

        boolean userIsStaff = role.contains("ADMIN") || role.contains("SALE") || role.contains("ORGANIZE");
        boolean userIsCustomer = role.contains("CUSTOMER");
        boolean isAnonymous = role.contains("ANONYMOUS");
        if(userIsStaff){
            return "redirect:/staff/dashboard";
        }
        model.addAttribute("loginDTO", new LoginDTO());
        return "adminTemplate/login";
    }

    @GetMapping("/dashboard")
    public String index(Model model,@CookieValue(name = "token", defaultValue = "") String token) {
        model.addAttribute("token",token);
        return "adminTemplate/home";
    }



    @GetMapping("/error")
    public String error()
    {
        return "adminTemplate/error";
    }

}


///staff/admin/addStaff
