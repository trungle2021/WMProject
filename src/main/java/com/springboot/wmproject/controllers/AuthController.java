package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.*;
import com.springboot.wmproject.services.AuthServices.AuthService;
import com.springboot.wmproject.services.AuthServices.CustomerAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthService authService;
    private CustomerAccountService accountService;

    @Autowired
    public AuthController(AuthService authService, CustomerAccountService accountService) {
        this.authService = authService;
        this.accountService = accountService;
    }

    //    Login API
    @PostMapping(value = {"/employee/login"})
    public ResponseEntity<JWTAuthResponse> staffLogin(@RequestBody LoginDTO loginDTO){
        String token = authService.employeeLogin(loginDTO);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping(value = {"/customer/login"})
    public ResponseEntity<JWTAuthResponse> customerLogin(@RequestBody LoginDTO loginDTO){
        String token = authService.customerLogin(loginDTO);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = {"/employee/register"})
    public ResponseEntity<RegisterDTO> staffRegister(@RequestBody RegisterDTO registerDTO){
        RegisterDTO response = authService.employeeRegister(registerDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = {"/customer/register"})
    public ResponseEntity<RegisterDTO> customerRegister(@RequestBody RegisterDTO registerDTO){
        RegisterDTO response = authService.customerRegister(registerDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/customer/processForgotPassword")
    public ResponseEntity<String> processForgotPassword(@RequestBody String email){
        CustomerAccountDTO accountDTO = accountService.findByEmail(email);
        if(accountDTO != null){
            return ResponseEntity.ok("AccountValid");
        }
        return ResponseEntity.ok("NOT FOUND");
    }
}
