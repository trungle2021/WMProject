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
import wm.clientmvc.DTO.EmployeeDTO;
import wm.clientmvc.DTO.OrganizeTeamDTO;
import wm.clientmvc.DTO.RegisterDTO;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.ClientUtilFunction;
import wm.clientmvc.utils.SD_CLIENT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/staff/employees")
public class EmployeeController {
    String api_create_employee = SD_CLIENT.DOMAIN_APP_API + "/api/auth/employees/create";
    String api_update_employee = SD_CLIENT.DOMAIN_APP_API +  "/api/auth/employees/update";
    String api_getOne_employee = SD_CLIENT.DOMAIN_APP_API +  "/api/auth/employees/getOne/RegisterEmployee/";

    String api_teams_all = SD_CLIENT.DOMAIN_APP_API + "/api/teams/all";


    @GetMapping("/create")
    public String createEmp(Model model,@CookieValue(name = "token", defaultValue = "") String token,RedirectAttributes attributes) throws JsonProcessingException {


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
                    api_teams_all,
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
                case "401":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/staff/login";
                case "404":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/404-not-found";
                case "403":
                    return "redirect:/access-denied";

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }catch(HttpServerErrorException e){
            return "redirect:/error";
        }
        return "adminTemplate/pages/create-employee";
    }

    @PostMapping("/create")
    public String createEmp(@Valid @ModelAttribute RegisterDTO registerDTO,BindingResult result, @CookieValue(name = "token", defaultValue = "") String token, RedirectAttributes attributes, @RequestParam("employee-create-pic") MultipartFile file) throws IOException {

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
                    api_create_employee,
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

            String status = String.valueOf(ex.getStatusCode().value());
            switch (status) {
                case "401":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/staff/login";
                default:
                    if (result.hasErrors()) {
                        attributes.addFlashAttribute("result",result);
                    }
                    attributes.addFlashAttribute("registerDTO",registerDTO);
                    attributes.addFlashAttribute("errorMessages", langs);
                    return "redirect:/staff/employees/create";

            }
        }
//        catch(HttpServerErrorException e){
//            return "redirect:/error";
//        }
        attributes.addFlashAttribute("message","Create Employee Success");
        return "redirect:/staff/employees/create";
    }

    @GetMapping("/update/{id}")
    public String updateEmp(@CookieValue(name = "token", defaultValue = "") String token,RedirectAttributes attributes,Model model,@PathVariable(name = "id") int id ) throws JsonProcessingException {

        BindingResult result = (BindingResult) model.asMap().get("result");
        if(result != null){
            model.addAttribute("result",result);
            model.addAttribute("registerDTO",model.asMap().get("registerDTO"));
        }


        ParameterizedTypeReference<List<OrganizeTeamDTO>> responseTypeTeam = new ParameterizedTypeReference<List<OrganizeTeamDTO>>() {};
        try {
            List<OrganizeTeamDTO> teamDTOList = APIHelper.makeApiCall(
                    api_teams_all,
                    HttpMethod.GET,
                    null,
                    token,
                    responseTypeTeam
            );
            model.addAttribute("teamList", teamDTOList);

            RegisterDTO registerDTO = APIHelper.makeApiCall(
                    api_getOne_employee + id,
                    HttpMethod.GET,
                    null,
                    token,
                    RegisterDTO.class);
            model.addAttribute("message",model.asMap().get("message"));
            model.addAttribute("registerDTO",registerDTO);

            model.addAttribute("errorMessages",model.asMap().get("errorMessages"));


        } catch (HttpClientErrorException ex) {
            String responseError = ex.getResponseBodyAsString();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(responseError, Map.class);
            String message = map.get("message").toString();

            String status = String.valueOf(ex.getStatusCode().value());
            switch (status) {
                case "401":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/staff/login";
                case "404":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/404-not-found";
                case "403":
                    return "redirect:/access-denied";

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        catch(HttpServerErrorException e){
//            return "redirect:/error";
//        }
        return "adminTemplate/pages/update-employee";
    }

    @PostMapping("/update")
    public String updateEmp(@Valid @ModelAttribute RegisterDTO registerDTO,BindingResult result, @CookieValue(name = "token", defaultValue = "") String token, RedirectAttributes attributes, @RequestParam("employee-create-pic") MultipartFile file) throws IOException {

        //xu ly avatar
        ClientUtilFunction utilFunction = new ClientUtilFunction();
        if(!file.isEmpty()){
            String avatar = utilFunction.AddFileEncrypted(file);
            registerDTO.setAvatar(avatar);
        }

//        registerDTO.setEmployeeId(re);
        try {
            RegisterDTO response_ =  APIHelper.makeApiCall(
                    api_update_employee,
                    HttpMethod.PUT,
                    registerDTO,
                    token,
                    RegisterDTO.class
            );

            if (result.hasErrors()) {
                attributes.addFlashAttribute("result",result);
                attributes.addFlashAttribute("registerDTO",registerDTO);
                return "redirect:/staff/employees/update/" + registerDTO.getEmployeeId();
            }

        }catch (HttpClientErrorException ex) {
            String responseError = ex.getResponseBodyAsString();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(responseError, Map.class);
            String message = map.get("message").toString();

            final ObjectMapper objectMapper = new ObjectMapper();
            String[] langs = objectMapper.readValue(message, String[].class);

            String status = String.valueOf(ex.getStatusCode().value());
            switch (status) {
                case "401":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/staff/login";
                default:
                    if (result.hasErrors()) {
                        attributes.addFlashAttribute("result",result);
                    }
                    attributes.addFlashAttribute("registerDTO",registerDTO);
                    attributes.addFlashAttribute("errorMessages", langs);
                    return "redirect:/staff/employees/update/" + registerDTO.getEmployeeId();

            }
        }
//        catch(HttpServerErrorException e){
//            return "redirect:/error";
//        }

        attributes.addFlashAttribute("message","Update Employee Success");
        return "redirect:/staff/employees/update/" + registerDTO.getEmployeeId();
    }



}
