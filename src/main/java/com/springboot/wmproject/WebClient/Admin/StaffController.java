package com.springboot.wmproject.WebClient.Admin;

import com.springboot.wmproject.DTO.LoginDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
