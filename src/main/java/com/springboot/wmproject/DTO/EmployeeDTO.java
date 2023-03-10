package com.springboot.wmproject.DTO;

import lombok.*;

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
    private String empType;
    private int team_id;
    private String avatar;
    private OrganizeTeamDTO organizeTeamsByTeamId;

}
