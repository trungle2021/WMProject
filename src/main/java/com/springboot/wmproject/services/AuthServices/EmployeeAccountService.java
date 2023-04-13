package com.springboot.wmproject.services.AuthServices;

import com.springboot.wmproject.DTO.EmployeeAccountDTO;
import com.springboot.wmproject.entities.EmployeeAccounts;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeAccountService {
    List<EmployeeAccountDTO> getAllEmployeeAccounts();

    EmployeeAccountDTO getEmployeeAccountByEmployeeAccountId(int id);
    EmployeeAccountDTO getEmployeeAccountByEmployeeId(int id);

    List<EmployeeAccountDTO> findByName(String name);
    List<EmployeeAccountDTO> filterByRole(String role);

    EmployeeAccountDTO create(EmployeeAccountDTO employeeAccountDTO);
    EmployeeAccountDTO update(EmployeeAccountDTO employeeAccountDTO);
    void delete(int id);
    List<EmployeeAccounts> checkUsernameExists(String username);

}
