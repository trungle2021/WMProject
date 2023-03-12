package wm.clientmvc.controllers.Admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wm.clientmvc.DTO.EmployeeDTO;
import wm.clientmvc.DTO.OrganizeTeamDTO;
import wm.clientmvc.DTO.VenueDTO;
import wm.clientmvc.DTO.VenueImgDTO;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.ClientUtilFunction;
import wm.clientmvc.utils.SD_CLIENT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/staff")
public class OrganizeTeamWebClientController {
    @GetMapping(value = "/team/index")
    public String getAll(Model model, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws IOException {
        ParameterizedTypeReference<List<EmployeeDTO>> responseTypeEmployee = new ParameterizedTypeReference<List<EmployeeDTO>>() {
        };
        ParameterizedTypeReference<List<OrganizeTeamDTO>> responseTypeTeam = new ParameterizedTypeReference<List<OrganizeTeamDTO>>() {
        };

        String msg = request.getParameter("msg");
        if (msg != null) {
            model.addAttribute("message", msg);
        }

        try {
            List<EmployeeDTO> employeeDTOList = APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/employees/all",
                    HttpMethod.GET,
                    null,
                    token,
                    responseTypeEmployee
            );
            List<OrganizeTeamDTO> teamDTOList = APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/teams/all",
                    HttpMethod.GET,
                    null,
                    token,
                    responseTypeTeam
            );
            model.addAttribute("employeeList", employeeDTOList);
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
        }
        return "adminTemplate/pages/team";
    }

    @PostMapping("/employee/create")
    public String createEmp(@ModelAttribute EmployeeDTO employeeDTO, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes, @RequestParam("employee-create-pic") MultipartFile[] files,@RequestParam("teamId") String teamId) throws IOException {
        //xu ly avatar
        ClientUtilFunction utilFunction = new ClientUtilFunction();
        List<String> empAvatar = utilFunction.AddMultipleFilesEncrypted(files);
        if (empAvatar != null) {
            for (String item : empAvatar
            ) {
                employeeDTO.setAvatar(item);
            }
        }
        employeeDTO.setTeam_id(Integer.parseInt(teamId));
        try {
            APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/employees/create",
                    HttpMethod.POST,
                    employeeDTO,
                    token,
                    EmployeeDTO.class
            );
        }catch (HttpClientErrorException ex) {
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
        }
        return "redirect:/admin/team?msg=Success";
    }
    @PostMapping("/admin/employee/update")
    public String updateEmp(@ModelAttribute EmployeeDTO employeeDTO, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes, @RequestParam("employee-update-pic") MultipartFile[] files,@RequestParam("teamId") String teamId) throws IOException {
        //xu ly avatar
        ClientUtilFunction utilFunction = new ClientUtilFunction();
        List<String> empAvatar = utilFunction.AddMultipleFilesEncrypted(files);
        if (empAvatar != null) {
            for (String item : empAvatar
            ) {
                employeeDTO.setAvatar(item);
            }
        }
        employeeDTO.setTeam_id(Integer.parseInt(teamId));
        try {
            APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/employees/update",
                    HttpMethod.PUT,
                    employeeDTO,
                    token,
                    EmployeeDTO.class
            );
        }catch (HttpClientErrorException ex) {
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
        }
        return "redirect:/admin/team?msg=Success";
    }
    @PostMapping("/admin/team/create")
    public String createTeam(@ModelAttribute OrganizeTeamDTO teamDTO, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws IOException {
        try {
            APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/teams/create",
                    HttpMethod.POST,
                    teamDTO,
                    token,
                    EmployeeDTO.class
            );
        }catch (HttpClientErrorException ex) {
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
        }
        return "redirect:/admin/team?msg=Success";
    }
}
