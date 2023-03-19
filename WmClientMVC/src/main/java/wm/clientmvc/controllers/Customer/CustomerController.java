package wm.clientmvc.controllers.Customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wm.clientmvc.DTO.CustomerDTO;
import wm.clientmvc.DTO.LoginDTO;
import wm.clientmvc.DTO.RegisterCustomerDTO;
import wm.clientmvc.securities.UserDetails.CustomUserDetails;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.ClientUtilFunction;
import wm.clientmvc.utils.SD_CLIENT;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static wm.clientmvc.utils.SD_CLIENT.api_customers_getOne_RegisterCustomer;
import static wm.clientmvc.utils.SD_CLIENT.api_customers_update;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    @GetMapping("/update/{id}")
    public String update(@CookieValue(name = "token", defaultValue = "") String token,RedirectAttributes attributes,Model model,@PathVariable(name = "id") int id ) throws JsonProcessingException {

        BindingResult result = (BindingResult) model.asMap().get("result");
        if(result != null){
            model.addAttribute("result",result);
            model.addAttribute("registerDTO",model.asMap().get("registerDTO"));
        }
        try {
            RegisterCustomerDTO registerDTO = APIHelper.makeApiCall(
                    api_customers_getOne_RegisterCustomer + id,
                    HttpMethod.GET,
                    null,
                    token,
                    RegisterCustomerDTO.class);
            model.addAttribute("message",model.asMap().get("message"));
            model.addAttribute("registerDTO",registerDTO);
            model.addAttribute("errorMessages",model.asMap().get("errorMessages"));

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            String message = "";
            String responseError = e.getResponseBodyAsString();
            String status = String.valueOf(e.getStatusCode().value());
            if(!responseError.isEmpty()){
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> map = mapper.readValue(responseError, Map.class);
                message = map.get("message").toString();
            }
            switch (status) {
                case "401":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/login";
                case "403":
                    return "/access-denied";
                case "404":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/404-not-found";
                default:
                    model.addAttribute("message",message);
                    return "adminTemplate/error";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "customerTemplate/update";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute RegisterCustomerDTO registerDTO, BindingResult result, @CookieValue(name = "token", defaultValue = "") String token, RedirectAttributes attributes, @RequestParam("employee-create-pic") MultipartFile file) throws IOException {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("result",result);
            attributes.addFlashAttribute("registerDTO",registerDTO);
            return "redirect:/customers/update/" + registerDTO.getCustomerId();
        }
        //xu ly avatar
        ClientUtilFunction utilFunction = new ClientUtilFunction();
        if(!file.isEmpty()){
            String avatar = utilFunction.AddFileEncrypted(file);
            registerDTO.setAvatar(avatar);
        }

        try {
            RegisterCustomerDTO response_ =  APIHelper.makeApiCall(
                    api_customers_update,
                    HttpMethod.PUT,
                    registerDTO,
                    token,
                    RegisterCustomerDTO.class
            );

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            if(response_.getCustomerId() == customUserDetails.getUserId().intValue()){
                ((CustomUserDetails) authentication.getPrincipal()).setFullName(response_.getFirst_name() + " " + response_.getLast_name());
            }



        }catch (HttpClientErrorException ex) {
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
            return "redirect:/customers/update/" + registerDTO.getCustomerId();

        }


        attributes.addFlashAttribute("message","Update Profile Success");
        return "redirect:/customers/update/" + registerDTO.getCustomerId();
    }

}
