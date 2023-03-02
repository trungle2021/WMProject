<<<<<<<< HEAD:src/main/java/com/springboot/wmproject/WebClient/Customer/HomeController.java
package com.springboot.wmproject.WebClient.Customer;
========
package com.springboot.wmproject.WebClient.Servlet.Staff;
>>>>>>>> develop:src/main/java/com/springboot/wmproject/WebClient/Servlet/Staff/HomeController.java

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

<<<<<<<< HEAD:src/main/java/com/springboot/wmproject/WebClient/Customer/HomeController.java
    @GetMapping(value = {"/index","/"})
    public String index(){
        return "home";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/about")
    public String about(){
        return "about";
========
    @GetMapping(value = {"/index", "/"})
    public String index() {
        return "adminTemplate/index";
    }

    @GetMapping("/about")
    public String about() {
        return "adminTemplate/pages/calendar";
>>>>>>>> develop:src/main/java/com/springboot/wmproject/WebClient/Servlet/Staff/HomeController.java
    }

    @GetMapping("/venue")
    public String venue() {
        return "adminTemplate/pages/gallery";
    }

}
