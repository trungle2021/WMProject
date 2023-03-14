package wm.clientmvc.controllers.Admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wm.clientmvc.DTO.LoginDTO;
import wm.clientmvc.utils.SD_CLIENT;

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
    public String index() {
        return "adminTemplate/home";
    }

    @GetMapping("/error")
    public String error()
    {
        return "adminTemplate/error";
    }
}


///staff/admin/addStaff
