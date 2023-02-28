package com.springboot.wmproject.WebClient.Servlet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/index")
    public String index(){
        return "adminTemplate/index";
    }

    @GetMapping("/about")
    public String about(){
        return "adminTemplate/pages/calendar";
    }

}
