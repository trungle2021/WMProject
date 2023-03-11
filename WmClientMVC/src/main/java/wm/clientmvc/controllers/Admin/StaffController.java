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

    @GetMapping("/login")
    public String employeeLogin(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String roleCheck = authentication.getAuthorities().stream().findFirst().toString();
        if(!roleCheck.contains("ANONYMOUS")){
            return "redirect:/staff/dashboard";
        }
        model.addAttribute("loginDTO",new LoginDTO());
        return "adminTemplate/login";
    }

    @GetMapping("/dashboard")
    public String index()
    {
        return "adminTemplate/home";
    }
}
