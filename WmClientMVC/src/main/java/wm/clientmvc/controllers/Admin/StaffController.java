package wm.clientmvc.controllers.Admin;

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
    public String employeeLogin(Model model){
        model.addAttribute("loginDTO",new LoginDTO());
        return "adminTemplate/login";
    }

    @GetMapping("/dashboard/index")
    public String index(@CookieValue(name = "token",defaultValue = "") String token)
    {
        if(token.equals("")){
            return "redirect:/staff/login";
        }
        return "adminTemplate/home";
    }
}
