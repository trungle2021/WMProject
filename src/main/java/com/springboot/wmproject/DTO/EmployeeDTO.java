package com.springboot.wmproject.DTO;

import com.springboot.wmproject.components.Auth.DTO.RegisterDTO;
import lombok.*;

import java.util.List;
import java.util.Set;

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
    private Double salary;
    private String email;
    private String gender;
    private boolean isLeader;
    private Integer teamId;
    private String avatar;
    private boolean isDeleted;
    private OrganizeTeamDTO organizeTeamsByTeamId;
    private List<EmployeeAccountDTO> employeeAccountsById;

    private EmployeeDTO registerDTO(EmployeeDTO employeeDTO, RegisterDTO registerDTO){
        employeeDTO.setName(registerDTO.getName().trim());
        employeeDTO.setSalary(registerDTO.getSalary());
        employeeDTO.setAddress(registerDTO.getAddress().trim());
        employeeDTO.setJoinDate(registerDTO.getJoinDate());
        employeeDTO.setTeamId(registerDTO.getTeam_id());
        employeeDTO.setGender(registerDTO.getGender());
        employeeDTO.setAvatar(registerDTO.getAvatar());
        employeeDTO.setDeleted(false);
        return employeeDTO;
    }
}
