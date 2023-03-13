package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.*;
import com.springboot.wmproject.services.AuthServices.AuthService;
import com.springboot.wmproject.services.AuthServices.CustomerAccountService;
import com.springboot.wmproject.services.AuthServices.PasswordResetTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthService authService;
    private CustomerAccountService accountService;

    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    public AuthController(PasswordResetTokenService passwordResetTokenService, AuthService authService, CustomerAccountService accountService) {
        this.authService = authService;
        this.accountService = accountService;
        this.passwordResetTokenService = passwordResetTokenService;
    }

    //    Login API
    @PostMapping(value = {"/employees/login"})
    public ResponseEntity<JWTAuthResponse> staffLogin(@RequestBody LoginDTO loginDTO){
        String token = authService.employeeLogin(loginDTO);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping(value = {"/customers/login"})
    public ResponseEntity<JWTAuthResponse> customerLogin(@RequestBody LoginDTO loginDTO){
        String token = authService.customerLogin(loginDTO);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = {"/employees/create"})
    public ResponseEntity<RegisterDTO> staffRegister(@RequestBody RegisterDTO registerDTO){
        RegisterDTO response = authService.employeeRegister(registerDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = {"/customers/register"})
    public ResponseEntity<RegisterCustomerDTO> customerRegister(@RequestBody RegisterCustomerDTO registerDTO){
        RegisterCustomerDTO response = authService.customerRegister(registerDTO);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/customers/processForgotPassword")
    public ResponseEntity<String> processForgotPassword(@RequestBody String email){
        String response = accountService.processForgotPassword(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/customers/processChangePassword")
    public ResponseEntity<String> processChangePassword(@RequestBody PasswordDTO passwordDTO) throws ParseException {
        String response = accountService.updatePassword(passwordDTO.getNewPassword(), passwordDTO.getToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/customers/createPWToken")
    public ResponseEntity<String> createPWToken(){
        String response = passwordResetTokenService.create(1);
        return ResponseEntity.ok(response);
    }
}
