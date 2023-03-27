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
import wm.clientmvc.DTO.*;
import wm.clientmvc.securities.UserDetails.CustomUserDetails;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.SD_CLIENT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static wm.clientmvc.utils.Static_Status.*;
import static wm.clientmvc.utils.Static_Status.orderStatusOrdered;

@Controller
public class HomeController {

    @GetMapping(value = {"/home","/customers/home","/"})
    public String home(Model model,@CookieValue(name="token",defaultValue = "")String token,@ModelAttribute("alertError")String alertError,@ModelAttribute("alertMessage")String alertMessage) {
//get venue image
            ParameterizedTypeReference<List<VenueImgDTO>> responseTypeVenueImg=new ParameterizedTypeReference<List<VenueImgDTO>>() {};
        try {
            List<VenueImgDTO> venueImgDTOList = APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/venuesImgs/all",
                    HttpMethod.GET,
                    null,
                    token,
                    responseTypeVenueImg
            );

            List<VenueImgDTO> venuesImages= new ArrayList<>();
            if(venueImgDTOList.size()>4 && venueImgDTOList.size()<8)
            {
                for (int i = 0; i <4; i++) {
                    venuesImages.add(venueImgDTOList.get(i));
                }
            }
            else if(venueImgDTOList.size()<4)
            {
                venuesImages=null;
            }
            else{
                for (int i = 0; i <8; i++) {
                    venuesImages.add(venueImgDTOList.get(i));
                }

            }

//get food image
        ParameterizedTypeReference<List<FoodDTO>> foodReference= new ParameterizedTypeReference<List<FoodDTO>>() {};
        List<FoodDTO> fList= APIHelper.makeApiCall(
          SD_CLIENT.DOMAIN_APP_API+"/api/food/all",
          HttpMethod.GET,
          null,
          token,
          foodReference);
        List<FoodDTO>foodList= new ArrayList<>();
        if(fList!=null && fList.size()>8)
        {
            for (int i = 0; i <8; i++) {
                foodList.add(fList.get(i));
            }
        }
        else{
         foodList=fList;
        }


        model.addAttribute("foodList",foodList);
        model.addAttribute("venuesImages",venuesImages);


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
        } catch (Exception e) {
            return "redirect:/error";
        }
    }
    @GetMapping(value = {"/error"})
    public String error(Model model,@ModelAttribute("alertError")String alertError,@ModelAttribute("alertMessage")String alertMessage) {


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


        return "404-not-found";
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
