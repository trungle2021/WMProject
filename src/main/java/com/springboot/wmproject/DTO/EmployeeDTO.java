package com.springboot.wmproject.DTO;

import lombok.Data;

import java.util.Set;

@Data
public class EmployeeDTO {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String joinDate;
    private Double salary;
    private String empType;
    private int team_id;

}
