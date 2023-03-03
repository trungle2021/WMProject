package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.EmployeeDTO;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> getAllEmployees();
    EmployeeDTO getEmployeeById(int id);
    EmployeeDTO create(EmployeeDTO newEmployeeDTO);
    EmployeeDTO update(EmployeeDTO updateEmployeeDTO);
    List<EmployeeDTO> findAllByEmpType(String empType);
    List<EmployeeDTO> findAllByName(String empType);
    void delete(int employeeId);

//    EmployeeDTO save(EmployeeDTO employeeDTO);

}
