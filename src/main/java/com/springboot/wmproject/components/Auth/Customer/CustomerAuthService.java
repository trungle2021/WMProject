package com.springboot.wmproject.components.Auth.Customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.wmproject.components.Auth.dto.LoginDTO;
import com.springboot.wmproject.components.Auth.dto.RegisterCustomerDTO;

import java.util.HashMap;

public interface CustomerAuthService {
    HashMap<String, String> customerLogin(LoginDTO loginDTO);

    RegisterCustomerDTO customerRegister(RegisterCustomerDTO registerDTO, String userAgent) throws JsonProcessingException;
    RegisterCustomerDTO customerPersonalValid(RegisterCustomerDTO registerDTO) throws JsonProcessingException;

    RegisterCustomerDTO customerUpdate(RegisterCustomerDTO registerDTO,String userAgent) throws JsonProcessingException;


    RegisterCustomerDTO getOneRegisterCustomer(int customerID) throws JsonProcessingException;

}
