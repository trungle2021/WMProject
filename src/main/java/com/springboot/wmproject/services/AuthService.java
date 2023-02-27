package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.LoginDTO;
import com.springboot.wmproject.DTO.RegisterDTO;

public interface AuthService {
    String login(LoginDTO loginDTO);
    String register(RegisterDTO registerDTO);
}
