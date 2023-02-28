package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.EmployeeDTO;
import com.springboot.wmproject.entities.Employees;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> getAllEmployees();
    EmployeeDTO getEmployeeById(int id);
    EmployeeDTO validEmployee(EmployeeDTO newEmployeeDTO);
    EmployeeDTO updateEmployee(EmployeeDTO updateEmployeeDTO);
    List<EmployeeDTO> findAllByEmpType(String empType);
    List<EmployeeDTO> findAllByName(String empType);
    void deleteEmployee(int employeeId);

    EmployeeDTO save(EmployeeDTO employeeDTO);

}
