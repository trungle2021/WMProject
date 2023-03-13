package wm.clientmvc.controllers.Admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wm.clientmvc.DTO.EmployeeDTO;
import wm.clientmvc.DTO.RegisterDTO;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.ClientUtilFunction;
import wm.clientmvc.utils.SD_CLIENT;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/staff/employees")
public class EmployeeController {
    String api_create_employee = "/api/auth/employees/create";
    @PostMapping("/create")
    public String createEmp(@Valid @ModelAttribute RegisterDTO registerDTO, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes, @RequestParam("employee-create-pic") MultipartFile file) throws IOException {
        RegisterDTO response_ = new RegisterDTO();

        //xu ly avatar
        ClientUtilFunction utilFunction = new ClientUtilFunction();
        if(!file.isEmpty()){
            String avatar = utilFunction.AddFileEncrypted(file);
                    registerDTO.setAvatar(avatar);
        }

        registerDTO.setTeam_id(registerDTO.getTeam_id());
        try {
            response_ =  APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + api_create_employee,
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
            String status = String.valueOf(ex.getStatusCode().value());
            switch (status) {
                case "401":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/staff/login";
                default:
                    attributes.addFlashAttribute("registerDTO",response_);
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/staff/employees/create";

            }
        }

        return "redirect:/staff/teams?msg=Success";

    }



    @PostMapping("/employees/update")
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
}
