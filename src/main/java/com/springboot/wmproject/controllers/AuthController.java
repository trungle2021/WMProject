package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.LoginDTO;
import com.springboot.wmproject.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //Login API
    @PostMapping(value = {"/login","/signin"})
    public ResponseEntity<String> login(LoginDTO loginDTO){
        String response = authService.login(loginDTO);
        return ResponseEntity.ok(response);
    }
}
