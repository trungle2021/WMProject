package wm.clientmvc.controllers.Admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import wm.clientmvc.DTO.OrganizeTeamDTO;
import wm.clientmvc.DTO.RegisterDTO;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.ClientUtilFunction;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static wm.clientmvc.utils.SD_CLIENT.*;

@Controller
@RequestMapping("/staff/employees")
public class EmployeeController {

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
                case "404":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/404-not-found";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }catch(HttpServerErrorException e){
            return "redirect:/error";
        }
        return "adminTemplate/pages/team_employees/create";
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

                if (result.hasErrors()) {
                    attributes.addFlashAttribute("result",result);
                }
                attributes.addFlashAttribute("registerDTO",registerDTO);
                attributes.addFlashAttribute("errorMessages", langs);
                return "redirect:/staff/employees/create";

        }
//        catch(HttpServerErrorException e){
//            return "redirect:/error";
//        }
        attributes.addFlashAttribute("message","Create Employee Success");
        return "redirect:/staff/employees/create";
    }

    @GetMapping("/update/{id}")
    public String update(@CookieValue(name = "token", defaultValue = "") String token,RedirectAttributes attributes,Model model,@PathVariable(name = "id") int id ) throws JsonProcessingException {

        BindingResult result = (BindingResult) model.asMap().get("result");
        if(result != null){
            model.addAttribute("result",result);
            model.addAttribute("registerDTO",model.asMap().get("registerDTO"));
        }
        try {
            RegisterDTO registerDTO = APIHelper.makeApiCall(
                    api_getOne_RegisterEmployee + id,
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
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/404-not-found";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        catch(HttpServerErrorException e){
//            return "redirect:/error";
//        }
        return "adminTemplate/pages/team_employees/update";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute RegisterDTO registerDTO,BindingResult result, @CookieValue(name = "token", defaultValue = "") String token, RedirectAttributes attributes, @RequestParam("employee-create-pic") MultipartFile file) throws IOException {

        //xu ly avatar
        ClientUtilFunction utilFunction = new ClientUtilFunction();
        if(!file.isEmpty()){
            String avatar = utilFunction.AddFileEncrypted(file);
            registerDTO.setAvatar(avatar);
        }

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
                if (result.hasErrors()) {
                    attributes.addFlashAttribute("result",result);
                }
                attributes.addFlashAttribute("registerDTO",registerDTO);
                attributes.addFlashAttribute("errorMessages", langs);
                return "redirect:/staff/employees/update/" + registerDTO.getEmployeeId();

        }
//        catch(HttpServerErrorException e){
//            return "redirect:/error";
//        }

        attributes.addFlashAttribute("message","Update Employee Success");
        return "redirect:/staff/employees/update/" + registerDTO.getEmployeeId();
    }

    @PostMapping(value = "/delete/{id}",produces = "application/json")
    @ResponseBody
    public Map<String, Object> delete(@PathVariable int id, @CookieValue(name = "token", defaultValue = "") String token) throws IOException {
        Map<String, Object> response = new HashMap<>();

        try{
            String response_api = APIHelper.makeApiCall(api_delete_employee + id,HttpMethod.DELETE,null,token,String.class);
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
