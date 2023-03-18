package wm.clientmvc.controllers.Customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customers/venue")
public class VenueController {
    @GetMapping("")
    public String IndexPage(){
        return "venue";
    }
}
