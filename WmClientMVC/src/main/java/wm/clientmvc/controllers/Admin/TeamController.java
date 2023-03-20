package wm.clientmvc.controllers.Admin;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wm.clientmvc.DTO.*;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.SD_CLIENT;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static wm.clientmvc.utils.SD_CLIENT.*;

@Controller
@RequestMapping("/staff/teams")
public class TeamController {
    @GetMapping(value = {"/getAll"})
    public String getAll(Model model, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, RedirectAttributes attributes) throws IOException {
        String message = (String) model.asMap().get("message");
        ParameterizedTypeReference<List<TeamSummaryDTO>> responseTypeTeam = new ParameterizedTypeReference<List<TeamSummaryDTO>>() {};
        String errorMessage = (String) model.asMap().get("errorMessage");


        try {

            List<TeamSummaryDTO> teamSummary = APIHelper.makeApiCall(
                    api_getSummaryTeamOrganization,
                    HttpMethod.GET,
                    null,
                    token,
                    responseTypeTeam
            );

            model.addAttribute("teamSummary", teamSummary);
            model.addAttribute("teams", new OrganizeTeamDTO());


            BindingResult result = (BindingResult) model.asMap().get("result");
            if(result != null || errorMessage != null){
                model.addAttribute("errorMessage",errorMessage);
                model.addAttribute("result",result);
                return "adminTemplate/pages/teams/index";

            }

        } catch (HttpClientErrorException  | HttpServerErrorException e) {
            String responseError = e.getResponseBodyAsString();
            String status = String.valueOf(e.getStatusCode().value());
            if(!responseError.isEmpty()){
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> map = mapper.readValue(responseError, Map.class);
                errorMessage = map.get("message").toString();
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
        model.addAttribute("message",message);

        return "adminTemplate/pages/teams/index";
    }



    @PostMapping("/create")
    public String createTeam(@Valid @ModelAttribute OrganizeTeamDTO teamDTO, BindingResult result, @CookieValue(name = "token", defaultValue = "") String token, Model model, RedirectAttributes attributes) throws IOException {
       String regexTeamName = "^TEAM\\s+(?!(?:administrator|admin|ADMINISTRATOR|ADMIN)\\b)[a-zA-Z]+(\\s+[^\\d\\s]+)*$";
        if(result.hasErrors()){
                attributes.addFlashAttribute("result",result);
                 return "redirect:/staff/teams/getAll";
        }
        if(teamDTO.getTeamName().matches(regexTeamName)){
            model.addAttribute("errorMessage","Input must start with 'TEAM ' and have at least one letter after it and do not contain 'admin or administrator' word.");
            return "redirect:/staff/teams/getAll";
        }
        try {
         OrganizeTeamDTO organizeTeamDTO =   APIHelper.makeApiCall(
                    api_teams_create,
                    HttpMethod.POST,
                    teamDTO,
                    token,
                    OrganizeTeamDTO.class
            );

        }catch (HttpClientErrorException  | HttpServerErrorException e) {
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
        attributes.addFlashAttribute("message","Create Team Success");
        return "redirect:/staff/teams/getAll";
    }

    @PostMapping("/update")
    public String updateTeam(@Valid @ModelAttribute OrganizeTeamDTO teamDTO, BindingResult result, @CookieValue(name = "token", defaultValue = "") String token, Model model, RedirectAttributes attributes) throws IOException {
        if(result.hasErrors()){
            String errorMessage = result.getFieldError().getDefaultMessage();
            attributes.addFlashAttribute("errorMessage",errorMessage);
            return "redirect:/staff/teams/getAll";
        }
        try {
            APIHelper.makeApiCall(
                    api_teams_update,
                    HttpMethod.PUT,
                    teamDTO,
                    token,
                    OrganizeTeamDTO.class
            );
        }catch (HttpClientErrorException  | HttpServerErrorException e) {
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
        attributes.addFlashAttribute("message","Update Team Success");
        return "redirect:/staff/teams/getAll";
    }


    @PostMapping(value = "/delete/{id}",produces = "application/json")
    @ResponseBody
    public Map<String, Object> deleteTeam(@PathVariable int id ,@CookieValue(name = "token", defaultValue = "") String token, Model model, RedirectAttributes attributes) throws IOException {

        Map<String, Object> response = new HashMap<>();

        try{
            String response_api = APIHelper.makeApiCall(api_teams_delete + id,HttpMethod.DELETE,null,token,String.class);
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
