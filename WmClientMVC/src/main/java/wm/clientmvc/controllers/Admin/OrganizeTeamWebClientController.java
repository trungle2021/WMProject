package wm.clientmvc.controllers.Admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import wm.clientmvc.DTO.EmployeeDTO;
import wm.clientmvc.DTO.OrganizeTeamDTO;
import wm.clientmvc.DTO.VenueDTO;
import wm.clientmvc.DTO.VenueImgDTO;

import java.util.List;

@Controller
public class OrganizeTeamWebClientController {
    @GetMapping(value = "/admin/team")
    public String getAll(Model model, HttpServletRequest request){
        String msg = request.getParameter("msg");
        if (msg != null) {
            model.addAttribute("message", msg);
        }
        String uriEmployee = "http://localhost:8080/api/employees/all";
        String uriTeam = "http://localhost:8080/api/teams/all";
        RestTemplate restTemplate = new RestTemplate();
        List<EmployeeDTO> employeeDTOList = restTemplate.getForObject(uriEmployee, List.class);
        List<OrganizeTeamDTO> teamDTOList = restTemplate.getForObject(uriTeam, List.class);
        ObjectMapper objectMapper = new ObjectMapper();
        model.addAttribute("employeeList", objectMapper.convertValue(employeeDTOList, new TypeReference<List<EmployeeDTO>>() {
        }));
        model.addAttribute("teamList", objectMapper.convertValue(teamDTOList, new TypeReference<List<OrganizeTeamDTO>>() {
        }));
        return "adminTemplate/pages/team";
    }
}
