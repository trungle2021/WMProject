package com.springboot.wmproject.components.Auth.Service;

import com.springboot.wmproject.components.Auth.DTO.LoginDTO;

import java.util.HashMap;

public interface AuthService<T> {
   HashMap<String, String> login(LoginDTO loginDTO);
   T register(T registerDTO);
}
