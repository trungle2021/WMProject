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
    private HttpSession session;
    @Value("${app-jwt-expiration-second}")
    private int jwtExpirationDate;

    @Autowired
    public AuthController(JwtTokenProvider tokenProvider,HttpSession session) {

        this.tokenProvider = tokenProvider;
        this.session = session;
    }
    public AuthController(){};
    //    STAFF


    @PostMapping(value = "/staff/login")
    public String loginStaff(@ModelAttribute("loginDTO") LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws JsonProcessingException {
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
        BindingResult result = (BindingResult) model.asMap().get("result");
        if(result != null){
            model.addAttribute("result",result);
            model.addAttribute("registerDTO",model.asMap().get("registerDTO"));
        }else{
            model.addAttribute("message",model.asMap().get("message"));
            RegisterCustomerDTO registerDTO = (RegisterCustomerDTO) model.asMap().get("registerDTO");
            if(registerDTO != null){
                model.addAttribute("registerDTO",registerDTO);
            }else{
                model.addAttribute("registerDTO",new RegisterCustomerDTO());
            }
        }

        model.addAttribute("errorMessages",model.asMap().get("errorMessages"));
        return "register";
    }

    @PostMapping("/register")
    public String registerCustomer(@Valid @ModelAttribute RegisterCustomerDTO registerDTO,BindingResult result,HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws IOException {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("result",result);
            attributes.addFlashAttribute("registerDTO",registerDTO);
            return "redirect:/register";
        }

        try {
            RegisterCustomerDTO responseRegister = APIHelper.makeApiCall(api_customerRegisterUrl, HttpMethod.POST, registerDTO, null, RegisterCustomerDTO.class);
            session.setAttribute("responseRegister",responseRegister);

            return "redirect:/sendVerifyEmail";
        } catch (HttpClientErrorException ex) {
            String responseError = ex.getResponseBodyAsString();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(responseError, Map.class);
            String message = map.get("message").toString();

            final ObjectMapper objectMapper = new ObjectMapper();
            String[] langs = objectMapper.readValue(message, String[].class);

            if (result.hasErrors()) {
                attributes.addFlashAttribute("result",result);
            }
            attributes.addFlashAttribute("registerDTO",registerDTO);
            attributes.addFlashAttribute("errorMessages", langs);
            return "redirect:/register";

        }
    }


    //    FUNCTION
    public String callApiLogin(String apiUrl, String successUrl, String has401ExceptionUrl, @Valid @ModelAttribute("loginDTO") LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws JsonProcessingException {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        CustomerDTO customerDTO = new CustomerDTO();
        String avatar = null;

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
                    String is_verified = tokenProvider.getIsVerified(token);
                    String fullName = "";
                    Set<GrantedAuthority> authorities = new HashSet<>();
                    authorities.add(new SimpleGrantedAuthority(userType));

                    if (apiUrl.contains("/api/auth/customers")) {
                        customerDTO = APIHelper.makeApiCall(
                                api_customers_getOne + userID,
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
                                api_employees_getOne + userID,
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


                   CustomUserDetails customerUserDetails = new CustomUserDetails(loginDTO.getUsername(), loginDTO.getPassword(), Long.parseLong(userID), fullName,avatar, Boolean.valueOf(is_verified),authorities);
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
        }
        return "redirect:" + logoutSuccessUrl + "?logout";
    }


}
