package wm.clientmvc.controllers.Auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wm.clientmvc.DTO.JWTAuthResponse;
import wm.clientmvc.DTO.LoginDTO;
import wm.clientmvc.securities.JWT.JwtTokenProvider;
import wm.clientmvc.securities.UserDetails.CustomUserDetails;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.SD_CLIENT;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;


@Controller
public class LoginController {
    String customerLoginUrl = SD_CLIENT.DOMAIN_APP_API + "/api/auth/customer/login";
    String staffLoginUrl = SD_CLIENT.DOMAIN_APP_API + "/api/auth/employee/login";
    JwtTokenProvider tokenProvider;
    @Autowired
    public LoginController(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
    @Value("${app-jwt-expiration-second}")
    private int jwtExpirationDate;


//    EMPLOYEE LOGIN
    @PostMapping(value = "/staff/login")
    public String loginEmployee(@ModelAttribute("loginDTO") LoginDTO loginDTO,HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws JsonProcessingException {
        return  callApiLogin(
                staffLoginUrl,
                "/staff/dashboard",
                "/staff/login",
                loginDTO,
                request,
                response,
                redirectAttributes);
    }

    @GetMapping(value = "/staff/logout")
    public String logoutStaff(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        return logout(request,response,"/staff/login");
    }
//    CUSTOMER LOGIN
    @PostMapping(value = "/customer/login")
    public String loginCustomer(@ModelAttribute("loginDTO") LoginDTO loginDTO,HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws JsonProcessingException {
        return  callApiLogin(
                customerLoginUrl,
                "/",
                "/customer/login",
                loginDTO,
                request,
                response,
                redirectAttributes);
    }

    @GetMapping(value = "/customer/logout")
    public String logoutCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException {
       return logout(request,response,"/customer/login");
    }


//    FUNCTION
    public String callApiLogin(String apiUrl,String successUrl,String has401ExceptionUrl,@ModelAttribute("loginDTO") LoginDTO loginDTO,HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws JsonProcessingException {
        try {
            JWTAuthResponse jwtAuthResponse = APIHelper.makeApiCall(
                    apiUrl,
                    HttpMethod.POST,
                    loginDTO,
                    null,
                    JWTAuthResponse.class);

            //Create and config for cookie. Store JWT token in cookie
            if(jwtAuthResponse != null){
                String token = jwtAuthResponse.getAccessToken();

                if(StringUtils.hasLength(token)){
                    Cookie cookie = new Cookie("token", token);
                    cookie.setMaxAge(jwtExpirationDate);
                    cookie.setSecure(true);
                    cookie.setHttpOnly(true);
                    cookie.setPath("/");
                    response.addCookie(cookie);

                    String userType = tokenProvider.getUserType(token);
                    String userID = tokenProvider.getUserID(token);

                    Set<GrantedAuthority> authorities = new HashSet<>();
                    authorities.add(new SimpleGrantedAuthority(userType));

                    CustomUserDetails customerUserDetails = new CustomUserDetails(loginDTO.getUsername(),loginDTO.getPassword(),Long.parseLong(userID),authorities);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(customerUserDetails, loginDTO.getPassword(),authorities);
                    SecurityContext securityContext = SecurityContextHolder.getContext();
                    securityContext.setAuthentication(authentication);
                    HttpSession session = request.getSession(true);
                    session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, securityContext);
                }
            }
            return "redirect:"+successUrl;

        } catch (HttpClientErrorException e) {
            String responseError = e.getResponseBodyAsString();
            if(StringUtils.hasLength(responseError)){
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> map = mapper.readValue(responseError, Map.class);
                String message = map.get("message").toString();
                String status = String.valueOf(e.getStatusCode().value());
                switch (status) {
                    case "403":
                        return "redirect:/access-denied";
                    default:
                        redirectAttributes.addFlashAttribute("errorMessage", message);
                        return "redirect:" + has401ExceptionUrl;
                }
            }


        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return "redirect:/error";
    }
    public String logout(HttpServletRequest request, HttpServletResponse response,String logoutSuccessUrl) throws ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.setInvalidateHttpSession(true);
            logoutHandler.setClearAuthentication(true);

            Cookie cookie = new Cookie("token",null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
            logoutHandler.logout(request,response,authentication);
            request.logout();
        }
        return "redirect:"+logoutSuccessUrl;
    }
}
