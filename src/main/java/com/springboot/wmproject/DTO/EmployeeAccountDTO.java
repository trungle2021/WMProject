package com.springboot.wmproject.DTO;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAccountDTO {
    private int id;
    private String username;
    private String password;
    private String role;
    private Integer employeeId;

}
