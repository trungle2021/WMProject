package com.springboot.wmproject.services.AuthServices;

import com.springboot.wmproject.DTO.EmployeeAccountDTO;

import java.util.List;

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
