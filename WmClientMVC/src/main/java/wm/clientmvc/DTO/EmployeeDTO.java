package wm.clientmvc.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String joinDate;
    private String gender;
    private Integer isLeader;
    private Double salary;
    private String email;
    private Integer team_id;
    private String avatar;
    private Integer is_deleted;
    private OrganizeTeamDTO organizeTeamsByTeamId;
    private List<EmployeeAccountDTO> employeeAccountsById;

}
