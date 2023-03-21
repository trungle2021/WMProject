package wm.clientmvc.controllers.Customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wm.clientmvc.DTO.CustomerDTO;
import wm.clientmvc.DTO.JWTAuthResponse;
import wm.clientmvc.DTO.LoginDTO;
import wm.clientmvc.DTO.OrderDTO;
import wm.clientmvc.securities.UserDetails.CustomUserDetails;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.SD_CLIENT;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static wm.clientmvc.utils.Static_Status.*;
import static wm.clientmvc.utils.Static_Status.orderStatusOrdered;

@Controller
public class HomeController {

    @GetMapping(value = {"/home","/customers/home","/"})
    public String home(Model model,@ModelAttribute("alertError")String alertError,@ModelAttribute("alertMessage")String alertMessage) {


//get alert

        if (!alertMessage.isEmpty()) {
            model.addAttribute("alertMessage", alertMessage);
        }
        else {
            model.addAttribute("alertMessage", null);
        }
        if (!alertError.isEmpty()) {
            model.addAttribute("alertError", alertError);
        }
        else {
            model.addAttribute("alertError", null);
        }


        return "home";
    }
    @GetMapping(value = {"/customers/dashboard",})
    public String dashboard(Model model,@CookieValue(name="token",defaultValue = "")String token, @ModelAttribute("alertMessage") String alertMessage,@ModelAttribute("alertError") String alertError) {
        model.addAttribute("warningSt",orderStatusWarning);
        model.addAttribute("cancelingSt",orderStatusCancel);
        model.addAttribute("confirmSt",orderStatusConfirm);
        model.addAttribute("depositedSt",orderStatusDeposited);
        model.addAttribute("orderedSt",orderStatusOrdered);

        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails custUserDetails= (CustomUserDetails) authentication.getPrincipal();
        Long customerId= custUserDetails.getUserId();
        String url="http://localhost:8080/api/orders/bycustomerd/"+customerId;
        ParameterizedTypeReference<List<OrderDTO>> typeReference=new ParameterizedTypeReference<List<OrderDTO>>() {};
        try {
            List<OrderDTO> list= APIHelper.makeApiCall(
                    url,
                    HttpMethod.GET,
                    null,
                    token,
                    typeReference

            );
            model.addAttribute("list",list);
        } catch (IOException e) {
            model.addAttribute("alertError", "Can't load your order infomation!Please try again ");
            return "/customerTemplate/dashboard";
        }



        //get list order of customer

//khang show alert
//            model.addAttribute("confirmMess",confirmCancel);
        if (!alertMessage.isEmpty()) {
            model.addAttribute("alertMessage", alertMessage);
        }
        else {
            model.addAttribute("alertMessage", null);
        }
        if (!alertError.isEmpty()) {
            model.addAttribute("alertError", alertError);
        }
        else {
            model.addAttribute("alertError", null);
        }


        return "/customerTemplate/dashboard";
    }

    @GetMapping(value = {"/about","/customers/about"})
    public String about() {
        return "about";
    }

    @GetMapping(value = {"/login"})
    public String customerLogin(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream().findFirst().toString();

        boolean userIsStaff = role.contains("ADMIN") || role.contains("SALE") || role.contains("ORGANIZE");
        boolean userIsCustomer = role.contains("CUSTOMER");
        boolean isAnonymous = role.contains("ANONYMOUS");
        if (userIsCustomer) {
            return "redirect:/customers/home";
        }
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }


}
