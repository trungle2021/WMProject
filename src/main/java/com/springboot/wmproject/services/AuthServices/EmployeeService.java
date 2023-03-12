package com.springboot.wmproject.services.AuthServices;

import com.springboot.wmproject.DTO.EmployeeDTO;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> getAllEmployees();
    EmployeeDTO getEmployeeById(int id);
    EmployeeDTO create(EmployeeDTO newEmployeeDTO);
    EmployeeDTO update(EmployeeDTO updateEmployeeDTO);
    List<EmployeeDTO> findAllByRole(String role);
    List<EmployeeDTO> findAllByName(String empType);
    List<EmployeeDTO> findAllByTeamId(Integer teamId);

    void delete(int employeeId);


}
