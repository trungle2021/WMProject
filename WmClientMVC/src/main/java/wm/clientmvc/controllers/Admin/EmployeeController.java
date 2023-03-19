package wm.clientmvc.controllers.Admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wm.clientmvc.DTO.EmployeeDTO;
import wm.clientmvc.DTO.OrganizeTeamDTO;
import wm.clientmvc.DTO.RegisterDTO;
import wm.clientmvc.securities.UserDetails.CustomUserDetails;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.ClientUtilFunction;

import java.io.IOException;
import java.util.*;

import static wm.clientmvc.utils.SD_CLIENT.*;

@Controller
@RequestMapping("/staff/employees")
public class EmployeeController {

    @GetMapping(value = {"/getAll"})
    public String getAll(Model model, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, RedirectAttributes attributes) throws IOException {
        ParameterizedTypeReference<List<EmployeeDTO>> responseTypeEmployee = new ParameterizedTypeReference<List<EmployeeDTO>>() {
        };

        String msg = request.getParameter("msg");
        if (msg != null) {
            model.addAttribute("message", msg);
        }

        try {
            List<EmployeeDTO> employeeDTOList = APIHelper.makeApiCall(
                    api_employees_getAll,
                    HttpMethod.GET,
                    null,
                    token,
                    responseTypeEmployee
            );

            model.addAttribute("employeeList", employeeDTOList);
            model.addAttribute("token", token);
        } catch (HttpClientErrorException  | HttpServerErrorException e) {
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
        }
        return "adminTemplate/pages/employees/index";
    }


    @GetMapping("/create")
    public String create(Model model,@CookieValue(name = "token", defaultValue = "") String token,RedirectAttributes attributes) throws JsonProcessingException {


        BindingResult result = (BindingResult) model.asMap().get("result");
        if(result != null){
            model.addAttribute("result",result);
            model.addAttribute("registerDTO",model.asMap().get("registerDTO"));
        }else{
            model.addAttribute("message",model.asMap().get("message"));
            RegisterDTO registerDTO = (RegisterDTO) model.asMap().get("registerDTO");
            if(registerDTO != null){
                model.addAttribute("registerDTO",registerDTO);
            }else{
                model.addAttribute("registerDTO",new RegisterDTO());
            }
        }

        model.addAttribute("errorMessages",model.asMap().get("errorMessages"));
        ParameterizedTypeReference<List<OrganizeTeamDTO>> responseTypeTeam = new ParameterizedTypeReference<List<OrganizeTeamDTO>>() {};
        try {
            List<OrganizeTeamDTO> teamDTOList = APIHelper.makeApiCall(
                    api_teams_getAll,
                    HttpMethod.GET,
                    null,
                    token,
                    responseTypeTeam
            );
            model.addAttribute("teamList", teamDTOList);

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }catch(HttpServerErrorException e){
            return "redirect:/error";
        }
        return "adminTemplate/pages/employees/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute RegisterDTO registerDTO,BindingResult result, @CookieValue(name = "token", defaultValue = "") String token, RedirectAttributes attributes, @RequestParam("employee-create-pic") MultipartFile file) throws IOException {

        //xu ly avatar
        ClientUtilFunction utilFunction = new ClientUtilFunction();
        if(!file.isEmpty()){
            String avatar = utilFunction.AddFileEncrypted(file);
                    registerDTO.setAvatar(avatar);
        }

        if (result.hasErrors()) {
            attributes.addFlashAttribute("result",result);
            attributes.addFlashAttribute("registerDTO",registerDTO);
            return "redirect:/staff/employees/create";
        }

        registerDTO.setTeam_id(registerDTO.getTeam_id());
        try {
            RegisterDTO response_ =  APIHelper.makeApiCall(
                    api_employee_create,
                    HttpMethod.POST,
                    registerDTO,
                    token,
                   RegisterDTO.class
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
                return "redirect:/staff/employees/create";

        }

        attributes.addFlashAttribute("message","Create Employee Success");
        return "redirect:/staff/employees/create";
    }

    @GetMapping("/update/{id}")
    public String update(@CookieValue(name = "token", defaultValue = "") String token,RedirectAttributes attributes,Model model,@PathVariable(name = "id") int id ) throws JsonProcessingException {
        ParameterizedTypeReference<List<OrganizeTeamDTO>> responseTypeTeam = new ParameterizedTypeReference<List<OrganizeTeamDTO>>() {};
        BindingResult result = (BindingResult) model.asMap().get("result");
        if(result != null){
            model.addAttribute("result",result);
            model.addAttribute("registerDTO",model.asMap().get("registerDTO"));
        }
        try {
            RegisterDTO registerDTO = APIHelper.makeApiCall(
                    api_employees_getOne_RegisterEmployee + id,
                    HttpMethod.GET,
                    null,
                    token,
                    RegisterDTO.class);

            List<OrganizeTeamDTO> teamDTOList = APIHelper.makeApiCall(
                    api_teams_getAll,
                    HttpMethod.GET,
                    null,
                    token,
                    responseTypeTeam
            );
            model.addAttribute("teamList", teamDTOList);
            model.addAttribute("message",model.asMap().get("message"));
            model.addAttribute("registerDTO",registerDTO);
            model.addAttribute("errorMessages",model.asMap().get("errorMessages"));

        } catch (HttpClientErrorException ex) {
            String responseError = ex.getResponseBodyAsString();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(responseError, Map.class);
            String message = map.get("message").toString();

            String status = String.valueOf(ex.getStatusCode().value());
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/404-not-found";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        catch(HttpServerErrorException e){
//            return "redirect:/error";
//        }
        return "adminTemplate/pages/employees/update";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute RegisterDTO registerDTO,BindingResult result, @CookieValue(name = "token", defaultValue = "") String token, RedirectAttributes attributes, @RequestParam("employee-create-pic") MultipartFile file) throws IOException {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("result",result);
            attributes.addFlashAttribute("registerDTO",registerDTO);
            return "redirect:/staff/employees/update/" + registerDTO.getEmployeeId();
        }
        //xu ly avatar
        ClientUtilFunction utilFunction = new ClientUtilFunction();
        if(!file.isEmpty()){
            String avatar = utilFunction.AddFileEncrypted(file);
            registerDTO.setAvatar(avatar);
        }

        try {
            RegisterDTO response_ =  APIHelper.makeApiCall(
                    api_employee_update,
                    HttpMethod.PUT,
                    registerDTO,
                    token,
                    RegisterDTO.class
            );
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            if(response_.getEmployeeId() == customUserDetails.getUserId().intValue()){
                ((CustomUserDetails) authentication.getPrincipal()).setFullName(response_.getName());
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
                return "redirect:/staff/employees/update/" + registerDTO.getEmployeeId();
        }
        attributes.addFlashAttribute("message","Update Employee Success");
        return "redirect:/staff/employees/update/" + registerDTO.getEmployeeId();
    }

    @PostMapping(value = "/delete/{id}",produces = "application/json")
    @ResponseBody
    public Map<String, Object> delete(@PathVariable int id, @CookieValue(name = "token", defaultValue = "") String token) throws IOException {
        Map<String, Object> response = new HashMap<>();

        try{
            String response_api = APIHelper.makeApiCall(api_employee_delete + id,HttpMethod.DELETE,null,token,String.class);
            response.put("result", "success");
            response.put("statusCode", 200);
            response.put("message", response_api);

        }catch (HttpClientErrorException e){
            String responseError = e.getResponseBodyAsString();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(responseError, Map.class);
            String message = map.get("message").toString();
            response.put("result", "success");
            response.put("statusCode", e.getStatusCode());
            response.put("message",message);
        }

        return response;
    }
}
