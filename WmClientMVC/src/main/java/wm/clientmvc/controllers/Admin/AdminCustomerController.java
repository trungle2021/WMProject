package wm.clientmvc.controllers.Admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wm.clientmvc.DTO.CustomerDTO;
import wm.clientmvc.DTO.RegisterCustomerDTO;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.ClientUtilFunction;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static wm.clientmvc.utils.SD_CLIENT.*;

@Controller
@RequestMapping("staff/customers")
public class AdminCustomerController {
    @GetMapping(value = {"/getAll"})
    public String getAll(Model model, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws IOException {
        ParameterizedTypeReference<List<CustomerDTO>> responseTypeEmployee = new ParameterizedTypeReference<List<CustomerDTO>>() {
        };


        try {
            List<CustomerDTO> customerDTOS = APIHelper.makeApiCall(
                    api_customers_getAll,
                    HttpMethod.GET,
                    null,
                    token,
                    responseTypeEmployee
            );

            model.addAttribute("customerDTOS", customerDTOS);
        } catch (HttpClientErrorException ex) {
            String responseError = ex.getResponseBodyAsString();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(responseError, Map.class);
            String message = map.get("message").toString();

            String status = String.valueOf(ex.getStatusCode().value());
            switch (status) {
                case "404":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/404-not-found";
            }
        }
        return "adminTemplate/pages/customers/index";
    }

    @GetMapping("/create")
    public String create(Model model,@CookieValue(name = "token", defaultValue = "") String token,RedirectAttributes attributes) throws JsonProcessingException {
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
        return "adminTemplate/pages/customers/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute RegisterCustomerDTO registerDTO, BindingResult result, @CookieValue(name = "token", defaultValue = "") String token, RedirectAttributes attributes, @RequestParam("employee-create-pic") MultipartFile file) throws IOException {

        //xu ly avatar
        ClientUtilFunction utilFunction = new ClientUtilFunction();
        if(!file.isEmpty()){
            String avatar = utilFunction.AddFileEncrypted(file);
            registerDTO.setAvatar(avatar);
        }

        if (result.hasErrors()) {
            attributes.addFlashAttribute("result",result);
            attributes.addFlashAttribute("registerDTO",registerDTO);
            return "redirect:/staff/customers/create";
        }


        try {
            RegisterCustomerDTO response_ =  APIHelper.makeApiCall(
                    api_customerRegisterUrl,
                    HttpMethod.POST,
                    registerDTO,
                    token,
                    RegisterCustomerDTO.class
            );
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
            return "redirect:/staff/customers/create";

        }
//        catch(HttpServerErrorException e){
//            return "redirect:/error";
//        }
        attributes.addFlashAttribute("message","Create Customer Success");
        return "redirect:/staff/customers/create";
    }

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
                    return "redirect:/staff/login";
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

        return "adminTemplate/pages/customers/update";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute RegisterCustomerDTO registerDTO,BindingResult result, @CookieValue(name = "token", defaultValue = "") String token, RedirectAttributes attributes, @RequestParam("employee-create-pic") MultipartFile file) throws IOException {

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

            if (result.hasErrors()) {
                attributes.addFlashAttribute("result",result);
                attributes.addFlashAttribute("registerDTO",registerDTO);
                return "redirect:/staff/customers/update/" + registerDTO.getCustomerId();
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
            return "redirect:/staff/customers/update/" + registerDTO.getCustomerId();

        }


        attributes.addFlashAttribute("message","Update Employee Success");
        return "redirect:/staff/customers/update/" + registerDTO.getCustomerId();
    }




}
