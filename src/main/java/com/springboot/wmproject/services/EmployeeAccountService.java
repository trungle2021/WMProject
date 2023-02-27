package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.EmployeeAccountDTO;
import com.springboot.wmproject.entities.EmployeeAccounts;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeAccountService {
    List<EmployeeAccountDTO> getAllEmployeeAccounts();

    EmployeeAccountDTO getEmployeeAccountByEmployeeAccountId(int id);

    List<EmployeeAccountDTO> findByName(String name);
    List<EmployeeAccountDTO> filterByEmpType(String empType);

    EmployeeAccountDTO createEmployeeAccount(EmployeeAccountDTO employeeAccountDTO);
    EmployeeAccountDTO updateEmployeeAccount(EmployeeAccountDTO employeeAccountDTO);
    void deleteEmployeeAccount(int employeeAccountId);

    EmployeeAccountDTO save(EmployeeAccountDTO employeeAccountDTO);
    //    EmployeeAccountDTO getEmployeeAccountByEmployeeId(int id);

    //    List<EmployeeAccountDTO> findByPhone(String phone);
}
