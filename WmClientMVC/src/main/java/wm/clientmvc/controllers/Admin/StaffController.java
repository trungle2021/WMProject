package wm.clientmvc.controllers.Admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wm.clientmvc.DTO.LoginDTO;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @GetMapping("/login")
    public String employeeLogin(Model model,@CookieValue(name = "token",defaultValue = "") String token){

        model.addAttribute("loginDTO",new LoginDTO());
        return "adminTemplate/login";
    }

    @GetMapping("/dashboard")
    public String index()
    {
        return "adminTemplate/home";
    }
}
