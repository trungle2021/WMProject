package com.springboot.wmproject.components.Auth.controller;

import com.springboot.wmproject.DTO.*;
import com.springboot.wmproject.components.Auth.dto.PasswordDTO;
import com.springboot.wmproject.components.Auth.service.AuthService;
import com.springboot.wmproject.components.Auth.PasswordResetTokenService;
import com.springboot.wmproject.components.Customer.CustomerAccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final CustomerAccountService accountService;




    @PostMapping("/customers/verifyEmailRegister/{token}")
    public ResponseEntity<String> verifyEmailRegister(@PathVariable("token") String token) throws ParseException {
        String response = accountService.verifyEmailRegister(token);
        return ResponseEntity.ok(response);
    }






}
