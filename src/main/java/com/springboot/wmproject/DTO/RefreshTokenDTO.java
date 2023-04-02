package com.springboot.wmproject.DTO;

import com.springboot.wmproject.entities.CustomerAccounts;
import com.springboot.wmproject.entities.EmployeeAccounts;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenDTO {
    private long id;
    private Integer customerId;
    private Integer employeeId;
    private String expiryDate;
    private String token;
    private CustomerAccountDTO customerAccountsByCustomerId;
    private EmployeeAccountDTO employeeAccountsByEmployeeId;
}
