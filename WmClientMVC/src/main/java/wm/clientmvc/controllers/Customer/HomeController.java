package wm.clientmvc.controllers.Customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wm.clientmvc.DTO.*;
import wm.clientmvc.controllers.Auth.AuthController;
import wm.clientmvc.securities.UserDetails.CustomUserDetails;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.SD_CLIENT;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static wm.clientmvc.utils.Static_Status.*;
import static wm.clientmvc.utils.Static_Status.orderStatusOrdered;

@Controller
public class HomeController {

    AuthController authController;

    private HttpSession session;

    @Autowired
    public HomeController(AuthController authController, HttpSession session) {
        this.authController = authController;
        this.session = session;
    }

    @GetMapping(value = {"/home", "/customers/home", "/"})
    public String home(Model model, @CookieValue(name = "token", defaultValue = "") String token, @ModelAttribute("alertError") String alertError, @ModelAttribute("alertMessage") String alertMessage,HttpServletRequest request,HttpServletResponse response) {
    //get venue image
        ParameterizedTypeReference<List<VenueImgDTO>> responseTypeVenueImg = new ParameterizedTypeReference<List<VenueImgDTO>>() {
        };
        try {
            List<VenueImgDTO> venueImgDTOList = APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/venuesImgs/all",
                    HttpMethod.GET,
                    null,
                    token,
                    responseTypeVenueImg,request,response
            );

            List<VenueImgDTO> venuesImages = new ArrayList<>();
            if (venueImgDTOList.size() > 4 && venueImgDTOList.size() < 8) {
                for (int i = 0; i < 4; i++) {
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
            ParameterizedTypeReference<List<FoodDTO>> foodReference = new ParameterizedTypeReference<List<FoodDTO>>() {
            };
            List<FoodDTO> fList = APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/food/all",
                    HttpMethod.GET,
                    null,
                    token,
                    foodReference,request,response);
            List<FoodDTO> foodList = new ArrayList<>();
            if (fList != null && fList.size() > 8) {
                for (int i = 0; i < 8; i++) {
                    foodList.add(fList.get(i));
                }
            } else {
                foodList = fList;
            }
//get all review
            ParameterizedTypeReference<List<ReviewDTO>> reviewTypeReference = new ParameterizedTypeReference<List<ReviewDTO>>() {
            };
            List<ReviewDTO>reviewDTOList=APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API+"/api/reviews/allActive",
                    HttpMethod.GET,
                    null,
                    token,
                    reviewTypeReference,request,response
            );

            model.addAttribute("foodList", foodList);
            model.addAttribute("venuesImages", venuesImages);
            model.addAttribute("reviewList",reviewDTOList);

//get alert

            if (!alertMessage.isEmpty()) {
                model.addAttribute("alertMessage", alertMessage);
            } else {
                model.addAttribute("alertMessage", null);
            }
            if (!alertError.isEmpty()) {
                model.addAttribute("alertError", alertError);
            } else {
                model.addAttribute("alertError", null);
            }

            return "home";
        } catch (Exception e) {
            return "redirect:/error";
        }
    }

    @GetMapping(value = {"/error"})
    public String error(Model model, @ModelAttribute("alertError") String alertError, @ModelAttribute("alertMessage") String alertMessage) {


//get alert

        if (!alertMessage.isEmpty()) {
            model.addAttribute("alertMessage", alertMessage);
        } else {
            model.addAttribute("alertMessage", null);
        }
        if (!alertError.isEmpty()) {
            model.addAttribute("alertError", alertError);
        } else {
            model.addAttribute("alertError", null);
        }


        return "404-not-found";
    }

    @GetMapping(value = {"/customers/dashboard",})
    public String dashboard(Model model, @CookieValue(name = "token", defaultValue = "") String token, @ModelAttribute("alertMessage") String alertMessage, @ModelAttribute("alertError") String alertError,HttpServletRequest request,HttpServletResponse response) {
        model.addAttribute("warningSt", orderStatusWarning);
        model.addAttribute("cancelingSt", orderStatusCancel);
        model.addAttribute("confirmSt", orderStatusConfirm);
        model.addAttribute("depositedSt", orderStatusDeposited);
        model.addAttribute("orderedSt", orderStatusOrdered);
        model.addAttribute("completedSt",orderStatusCompleted);
        model.addAttribute("refundSt",orderStatusRefund);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails custUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long customerId = custUserDetails.getUserId();
        String url = "http://localhost:8080/api/orders/bycustomerd/" + customerId;
        ParameterizedTypeReference<List<OrderDTO>> typeReference = new ParameterizedTypeReference<List<OrderDTO>>() {
        };
        try {
            List<OrderDTO> list = APIHelper.makeApiCall(
                    url,
                    HttpMethod.GET,
                    null,
                    token,
                    typeReference,request,response

            );
            model.addAttribute("list", list);
        } catch (IOException e) {
            model.addAttribute("alertError", "Can't load your order infomation!Please try again ");
            return "/customerTemplate/dashboard";
        }


        //get list order of customer

//khang show alert
//            model.addAttribute("confirmMess",confirmCancel);
        if (!alertMessage.isEmpty()) {
            model.addAttribute("alertMessage", alertMessage);
        } else {
            model.addAttribute("alertMessage", null);
        }
        if (!alertError.isEmpty()) {
            model.addAttribute("alertError", alertError);
        } else {
            model.addAttribute("alertError", null);
        }


        return "/customerTemplate/dashboard";
    }

    @GetMapping(value = {"/about", "/customers/about"})
    public String about() {
        return "about";
    }
    @GetMapping(value = {"/contact", "/customers/contact"})
    public String contact() {
        return "contact";
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

    @GetMapping("/sendVerifyEmail")
    public String sendVerifyEmail(@RequestParam(value = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws IOException {
        if(!token.isEmpty()){
           try{
               String _response = APIHelper.makeApiCall(SD_CLIENT.api_verify_email + token,HttpMethod.POST,null,null,String.class,request,response);
                   return "redirect:/login";
           }catch(HttpClientErrorException ex){
               String responseError = ex.getResponseBodyAsString();
               ObjectMapper mapper = new ObjectMapper();
               Map<String, Object> map = mapper.readValue(responseError, Map.class);
               String message = map.get("message").toString();

               String status = String.valueOf(ex.getStatusCode().value());
               switch (status) {
                   case "400":
                       attributes.addFlashAttribute("errorMessage", message);
                       return "redirect:/register";
                   case "404":
                       attributes.addFlashAttribute("errorMessage", message);
                       return "redirect:/404-not-found";
               }
           }
        }
        return "sendVerifyEmail";
    }
}
