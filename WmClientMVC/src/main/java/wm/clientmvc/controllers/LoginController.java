package wm.clientmvc.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wm.clientmvc.DTO.JWTAuthResponse;
import wm.clientmvc.DTO.LoginDTO;
import wm.clientmvc.utils.SD_CLIENT;
import java.io.IOException;
import java.util.Arrays;


@Controller
public class LoginController {
    String customerLoginUrl = SD_CLIENT.DOMAIN_APP_API + "/api/auth/customer/login";
    String employeeLoginUrl = SD_CLIENT.DOMAIN_APP_API + "/api/auth/employee/login";

    @Value("${app-jwt-expiration-second}")
    private int jwtExpirationDate;


    //    EMPLOYEE LOGIN



    @PostMapping(value = "/staff/login")
    public String loginEmployee(@ModelAttribute("loginDTO") LoginDTO loginDTO, HttpServletResponse response, RedirectAttributes redirectAttributes){
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<LoginDTO> entity = new HttpEntity<>(loginDTO,headers);
            ResponseEntity<JWTAuthResponse> jwtAuthResponse = restTemplate.exchange(employeeLoginUrl, HttpMethod.POST, entity, JWTAuthResponse.class);

            //Create and config for cookie. Store JWT token in cookie
            Cookie cookie = new Cookie("token", jwtAuthResponse.getBody().getAccessToken());
            cookie.setMaxAge(jwtExpirationDate);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:/staff/dashboard/index";

        } catch (HttpClientErrorException e) {
            // Handle the exception
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                // Return an error message to the view
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid username or password");
                return "redirect:/staff/login";
            } else {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while logging in");
                return "redirect:/error";
            }
        }
    }

    @PostMapping(value = "/staff/logout")
    public String logoutEmployee(HttpServletRequest request,HttpServletResponse response, RedirectAttributes redirectAttributes) throws ServletException, IOException {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        request.logout();

        return "redirect:/staff/login";
    }

//    CUSTOMER LOGIN


    @PostMapping(value = "/customer/login")
    public String loginCustomer(@ModelAttribute("loginDTO") LoginDTO loginDTO, HttpServletResponse response, RedirectAttributes redirectAttributes){
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<LoginDTO> entity = new HttpEntity<>(loginDTO,headers);
            ResponseEntity<JWTAuthResponse> jwtAuthResponse = restTemplate.exchange(customerLoginUrl, HttpMethod.POST, entity, JWTAuthResponse.class);

            //Create and config for cookie. Store JWT token in cookie
            Cookie cookie = new Cookie("token", jwtAuthResponse.getBody().getAccessToken());
            cookie.setMaxAge((int)jwtExpirationDate);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "home";

        } catch (HttpClientErrorException e) {

            // Handle the exception
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                // Return an error message to the view
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid username or password");
                return "redirect:/login";
            } else {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while logging in");
                return "redirect:/error";
            }
        }
    }

    @GetMapping("/getCookie")
    public ResponseEntity<String> getCookie(@CookieValue(name = "token",defaultValue = "") String token) {
       return ResponseEntity.ok(token) ;
    }







}
