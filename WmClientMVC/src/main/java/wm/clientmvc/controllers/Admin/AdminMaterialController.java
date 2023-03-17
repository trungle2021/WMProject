package wm.clientmvc.controllers.Admin;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wm.clientmvc.DTO.OrderDTO;
import wm.clientmvc.utils.APIHelper;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/staff/material")
public class AdminMaterialController {
@RequestMapping(value="/detail/{id}",method = RequestMethod.GET)
public String showMaterial(Model model, @PathVariable Integer id, @CookieValue(name="token",defaultValue = "")String token)
{
    String url="http://localhost:8080/api/orders/"+id;
    try {
        OrderDTO order= APIHelper.makeApiCall(
            url,
                HttpMethod.GET,
                null,
                token,
                OrderDTO.class
        );
        model.addAttribute("OrderDTO",order);
        return "adminTemplate/pages/organize/material";
    } catch (IOException e) {
        model.addAttribute("message",e.getMessage());
        return "adminTemplate/error";
    }
}


}
