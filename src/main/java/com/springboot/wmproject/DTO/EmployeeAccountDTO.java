package com.springboot.wmproject.DTO;

import lombok.Data;

import java.util.Set;

@Data
public class EmployeeAccountDTO {
    private int id;
    private String username;
    private String password;
    private String role;
    private Integer employeeId;

}
