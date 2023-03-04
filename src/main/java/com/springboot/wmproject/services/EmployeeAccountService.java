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

    EmployeeAccountDTO create(EmployeeAccountDTO employeeAccountDTO);
    EmployeeAccountDTO update(EmployeeAccountDTO employeeAccountDTO);
    void delete(int id);

//    EmployeeAccountDTO save(EmployeeAccountDTO employeeAccountDTO);

}
