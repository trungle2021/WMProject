package wm.clientmvc.controllers.Customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import wm.clientmvc.DTO.LoginDTO;

@Controller
public class HomeController {

    @GetMapping(value = {"/index","/"})
    public String index(){
        return "home";
    }

    @GetMapping("/customer/login")
    public String customerLogin(Model model){
        model.addAttribute("loginDTO",new LoginDTO());
        return "login";
    }

    @GetMapping("/customer/about")
    public String about() {
        return "about";

    }
}
