package com.springboot.wmproject.components.Auth.Employee;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.wmproject.components.Auth.dto.LoginDTO;
import com.springboot.wmproject.components.Auth.dto.RegisterDTO;

import java.util.HashMap;

public interface EmployeeAuthService {
    HashMap<String, String> employeeLogin(LoginDTO loginDTO);
    RegisterDTO employeeRegister(RegisterDTO registerDTO) throws JsonProcessingException;
    RegisterDTO employeeUpdate(RegisterDTO registerDTO) throws JsonProcessingException;
    RegisterDTO getOneRegisterEmp(int empID) throws JsonProcessingException;
    String staffDelete(int id);

    String findRoleByEmployeeID(int empID);
}
