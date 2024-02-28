package com.springboot.wmproject.components.Auth.Controller;

import com.springboot.wmproject.components.Auth.DTO.PasswordDTO;
import com.springboot.wmproject.components.Customer.CustomerAccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@AllArgsConstructor
public class PasswordController {

    private final CustomerAccountService accountService;
    @PostMapping("/customers/processForgotPassword")
    public ResponseEntity<String> processForgotPassword(@RequestBody String email, @RequestHeader("User-Agent") String userAgent){
        String response = accountService.processForgotPassword(email,userAgent);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/customers/processChangePassword")
    public ResponseEntity<String> processChangePassword(@RequestBody PasswordDTO passwordDTO) throws ParseException {
        String response = accountService.updatePassword(passwordDTO.getNewPassword(), passwordDTO.getToken());
        return ResponseEntity.ok(response);
    }


    @PostMapping("/customers/updatePasswordMobile")
    public ResponseEntity<String> updatePasswordMobile(@RequestBody PasswordDTO passwordDTO) throws ParseException {
        String response = accountService.updatePasswordMobile(passwordDTO.getNewPassword(),passwordDTO.getToken());
        return ResponseEntity.ok(response);
    }

}
