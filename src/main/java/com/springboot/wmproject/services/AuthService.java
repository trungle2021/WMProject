package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.LoginDTO;

public interface AuthService {
    String login(LoginDTO loginDTO);
}
