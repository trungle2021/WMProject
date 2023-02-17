package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.BookingDTO;
import com.springboot.wmproject.DTO.EmployeeAccountDTO;
import com.springboot.wmproject.entities.EmployeeAccounts;

import java.util.List;

public interface EmployeeAccountService {
    List<EmployeeAccountDTO> getAllEmployeeAccounts();
    List<EmployeeAccountDTO> getEmployeeAccountByEmployeeId(int id);
    EmployeeAccountDTO getEmployeeAccountByEmployeeAccountId(int id);
    EmployeeAccountDTO createEmployeeAccount(EmployeeAccountDTO employeeAccountDTO);
    EmployeeAccountDTO updateEmployeeAccount(EmployeeAccountDTO employeeAccountDTO);
    void deleteEmployeeAccount(int employeeAccountId);
}
