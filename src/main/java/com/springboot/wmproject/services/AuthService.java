package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.LoginDTO;
import com.springboot.wmproject.DTO.RegisterDTO;

public interface AuthService {
    String employeeLogin(LoginDTO loginDTO);
    String customerLogin(LoginDTO loginDTO);
    RegisterDTO employeeRegister(RegisterDTO registerDTO);
    RegisterDTO customerRegister(RegisterDTO registerDTO);
}
