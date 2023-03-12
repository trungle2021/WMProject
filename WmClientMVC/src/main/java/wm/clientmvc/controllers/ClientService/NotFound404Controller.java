package wm.clientmvc.controllers.ClientService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class NotFound404Controller {
    @GetMapping("/404-not-found")
    public String get404() {
        return "404-not-found";
    }
}
