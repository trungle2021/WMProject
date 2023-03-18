package wm.clientmvc.controllers.Customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/customers/food")
public class MenuController {
    @GetMapping(value = "")
    public String IndexPage(){
        return "food";
    }
    @GetMapping(value = "/detail")
    public String FoodDetail(){
        return "food-detail-client";
    }
}
