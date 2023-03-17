package wm.clientmvc.controllers.Auth;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wm.clientmvc.DTO.*;
import wm.clientmvc.securities.JWT.JwtTokenProvider;
import wm.clientmvc.securities.UserDetails.CustomUserDetails;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.SD_CLIENT;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
import static wm.clientmvc.utils.SD_CLIENT.*;


@Controller
public class AuthController {

    JwtTokenProvider tokenProvider;
    @Value("${app-jwt-expiration-second}")
    private int jwtExpirationDate;

    @Autowired
    public AuthController(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    //    STAFF


    @PostMapping(value = "/staff/login")
    public String loginEmployee(@ModelAttribute("loginDTO") LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws JsonProcessingException {
        return callApiLogin(
                SD_CLIENT.api_staffLoginUrl,
                "/staff/dashboard",
                "/staff/login",
                loginDTO,
                request,
                response,
                redirectAttributes);
    }

    @GetMapping(value = "/staff/logout")
    public String logoutStaff(Model model, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        return logout(model, request, response, "/staff/login");
    }

    //    CUSTOMER
    @GetMapping(value = "/customers/logout")
    public String logoutCustomer(Model model, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        return logout(model, request, response, "/login");
    }
    @PostMapping(value = "/login")
    public String loginCustomer(@ModelAttribute("loginDTO") LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws JsonProcessingException {
        return callApiLogin(
                api_customerLoginUrl,
                "/customers/home",
                "/login",
                loginDTO,
                request,
                response,
                redirectAttributes);
    }



    @GetMapping("/register")
    public String registerCustomer(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream().findFirst().toString();

        boolean userIsCustomer = role.contains("CUSTOMER");

        if(userIsCustomer){
            return "redirect:/customers/home";
        }
        RegisterCustomerDTO registerCustomerDTO = new RegisterCustomerDTO();
        model.addAttribute("registerCustomerDTO", registerCustomerDTO);
        return "register";
    }

    @PostMapping("/register")
    public String registerCustomer(@Valid @ModelAttribute RegisterCustomerDTO registerCustomerDTO,HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return "register"; // return to the registration page with error messages
        }
        try {
            RegisterCustomerDTO responseRegister = APIHelper.makeApiCall(api_customerRegisterUrl, HttpMethod.POST, registerCustomerDTO, null, RegisterCustomerDTO.class);
            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setUsername(responseRegister.getUsername());
            loginDTO.setPassword(responseRegister.getPassword());
            return callApiLogin(
                    api_customerLoginUrl,
                    "/",
                    "/customers/login",
                    loginDTO,
                    request,
                    response,
                    redirectAttributes);
        } catch (HttpClientErrorException e) {
            String responseError = e.getResponseBodyAsString();
            if (StringUtils.hasLength(responseError)) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> map = mapper.readValue(responseError, Map.class);
                String message = map.get("message").toString();
                String status = String.valueOf(e.getStatusCode().value());
                switch (status) {
                    case "403":
                        return "redirect:/access-denied";
                    default:
                        redirectAttributes.addFlashAttribute("errorMessage", message);
                        return "redirect:/customers/register";
                }
            }
        }

        return "redirect:/customers/login";
    }


    //    FUNCTION
    public String callApiLogin(String apiUrl, String successUrl, String has401ExceptionUrl, @Valid @ModelAttribute("loginDTO") LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws JsonProcessingException {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        CustomerDTO customerDTO = new CustomerDTO();
        String avatar = "";


        try {
            JWTAuthResponse jwtAuthResponse = APIHelper.makeApiCall(
                    apiUrl,
                    HttpMethod.POST,
                    loginDTO,
                    null,
                    JWTAuthResponse.class);

            //Create and config for cookie. Store JWT token in cookie
            if (jwtAuthResponse != null) {
                String token = jwtAuthResponse.getAccessToken();

                if (StringUtils.hasLength(token)) {
                    Cookie cookie = new Cookie("token", token);
                    cookie.setMaxAge(jwtExpirationDate);
                    cookie.setSecure(true);
                    cookie.setHttpOnly(true);
                    cookie.setPath("/");
                    response.addCookie(cookie);

                    String userType = tokenProvider.getUserType(token);
                    String userID = tokenProvider.getUserID(token);
                    String fullName = "";
                    Set<GrantedAuthority> authorities = new HashSet<>();
                    authorities.add(new SimpleGrantedAuthority(userType));

                    if (apiUrl.contains("/api/auth/customers")) {
                        customerDTO = APIHelper.makeApiCall(
                                api_getOne_customer + userID,
                                HttpMethod.GET,
                                null,
                                token,
                                CustomerDTO.class);
                        if(customerDTO.getAvatar() == null){
                            avatar = avatarDefault;
                        }else{
                            avatar = customerDTO.getAvatar();
                        }
                        fullName = customerDTO.getFirst_name() + " " +  customerDTO.getLast_name();


                    }else if(apiUrl.contains("/api/auth/employees")){
                        employeeDTO = APIHelper.makeApiCall(
                                api_getOne_employee + userID,
                                HttpMethod.GET,
                                null,
                                token,
                                EmployeeDTO.class);
                        if(employeeDTO.getAvatar() == null){
                            avatar = avatarDefault;
                        }else{
                            avatar = employeeDTO.getAvatar();
                        }
                        fullName = employeeDTO.getName();
                    }


                   CustomUserDetails customerUserDetails = new CustomUserDetails(loginDTO.getUsername(), loginDTO.getPassword(), Long.parseLong(userID), fullName,avatar, authorities);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(customerUserDetails, loginDTO.getPassword(), authorities);


                    SecurityContext securityContext = SecurityContextHolder.getContext();
                    securityContext.setAuthentication(authentication);
                    HttpSession session = request.getSession(true);
                    session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, securityContext);
                }
            }
            return "redirect:" + successUrl;

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            String status = String.valueOf(e.getStatusCode().value());

            String responseError = e.getResponseBodyAsString();
            if (StringUtils.hasLength(responseError)) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
                Map<String, String> map = mapper.readValue(responseError, Map.class);
                String message = map.get("message").toString();
                redirectAttributes.addFlashAttribute("errorMessage", message);
                return "redirect:" + has401ExceptionUrl;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/error";
    }

    public String logout(Model model, HttpServletRequest request, HttpServletResponse response, String logoutSuccessUrl) throws ServletException {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
//            SecurityContextHolder.clearContext();
//            model.asMap().clear();
        }
        return "redirect:" + logoutSuccessUrl + "?logout";
    }


}
