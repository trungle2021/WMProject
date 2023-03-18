package com.springboot.wmproject.services.AuthServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.wmproject.DTO.GenericResponse;
import com.springboot.wmproject.DTO.LoginDTO;
import com.springboot.wmproject.DTO.RegisterCustomerDTO;
import com.springboot.wmproject.DTO.RegisterDTO;

public interface AuthService {
    String findRoleByEmployeeID(int empID);
    String staffDelete(int id);
    String employeeLogin(LoginDTO loginDTO);
    String customerLogin(LoginDTO loginDTO);
    RegisterDTO employeeRegister(RegisterDTO registerDTO) throws JsonProcessingException;
    RegisterCustomerDTO customerRegister(RegisterCustomerDTO registerDTO) throws JsonProcessingException;
    RegisterDTO employeeUpdate(RegisterDTO registerDTO) throws JsonProcessingException;
    RegisterCustomerDTO customerUpdate(RegisterCustomerDTO registerDTO) throws JsonProcessingException;

    RegisterDTO getOneRegisterEmp(int empID) throws JsonProcessingException;
    RegisterCustomerDTO getOneRegisterCustomer(int customerID) throws JsonProcessingException;





}
