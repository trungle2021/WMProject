package com.springboot.wmproject.components.Auth.Controller;

import com.springboot.wmproject.DTO.JWTAuthResponse;
import com.springboot.wmproject.components.Auth.Password.PasswordResetTokenService;
import com.springboot.wmproject.components.Auth.Service.AuthService;
import com.springboot.wmproject.components.Customer.CustomerAccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@AllArgsConstructor
public class TokenController {


    private final CustomerAccountService accountService;

    private final PasswordResetTokenService passwordResetTokenService;

    @PostMapping(value = {"/refreshToken/{refreshToken}"})
    public ResponseEntity<JWTAuthResponse> refreshToken(@PathVariable("refreshToken") String refreshToken){
        String newAccessToken = authService.refreshToken(refreshToken);
        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(newAccessToken);
        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping("/customers/validToken")
    public ResponseEntity<String> validToken(@RequestBody String token) throws ParseException {
        String response = accountService.validToken(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/customers/createPWToken")
    public ResponseEntity<String> createPWToken(){
        String response = passwordResetTokenService.create(1);
        return ResponseEntity.ok(response);
    }
}
