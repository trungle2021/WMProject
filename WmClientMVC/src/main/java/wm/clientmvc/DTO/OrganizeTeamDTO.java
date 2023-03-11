package wm.clientmvc.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class OrganizeTeamDTO {
    private int id;
    private String teamName;
    @JsonIgnore
    private List<EmployeeDTO> employeesById;
}
