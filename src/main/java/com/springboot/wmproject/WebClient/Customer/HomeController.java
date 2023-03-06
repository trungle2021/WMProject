package com.springboot.wmproject.WebClient.Customer;

import com.springboot.wmproject.DTO.LoginDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
