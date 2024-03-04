package com.springboot.wmproject.components.Auth.CustomerAuth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.wmproject.DTO.JWTAuthResponse;
import com.springboot.wmproject.components.Auth.DTO.LoginDTO;
import com.springboot.wmproject.components.Auth.DTO.RegisterCustomerDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@AllArgsConstructor
@RequestMapping("/api/customer/auth")
public class CustomerAuthController {
    private final CustomerAuthService customerAuthService;

    @PostMapping(value = {"/customers/login"})
    public ResponseEntity<JWTAuthResponse> customerLogin(@RequestBody LoginDTO loginDTO) {
        HashMap<String, String> map = customerAuthService.login(loginDTO);
        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(map.get("accessToken"));
        jwtAuthResponse.setRefreshToken(map.get("refreshToken"));
        return ResponseEntity.ok(jwtAuthResponse);
    }

    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = {"/customers/register"})
    public ResponseEntity<RegisterCustomerDTO> customerRegister(@RequestBody RegisterCustomerDTO registerDTO, @RequestHeader("User-Agent") String userAgent) throws JsonProcessingException {
//        RegisterCustomerDTO response = customerAuthService.register(registerDTO, userAgent);
        RegisterCustomerDTO response = customerAuthService.register(registerDTO);
        return ResponseEntity.ok(response);
    }

//    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
//    @PostMapping(value = {"/customers/register/validPhoneEmail"})
//    public ResponseEntity<RegisterCustomerDTO> customerValidPhoneEmail(@RequestBody RegisterCustomerDTO registerDTO) throws JsonProcessingException {
//        RegisterCustomerDTO response = customerAuthService.customerPersonalValid(registerDTO);
//        return ResponseEntity.ok(response);
//    }

//    @PreAuthorize("hasAnyRole('ADMIN','SALE','CUSTOMER')")
//    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
//    @PutMapping(value = {"/customers/update"})
//    public ResponseEntity<RegisterCustomerDTO> customerUpdate(@RequestBody RegisterCustomerDTO registerDTO,@RequestHeader("User-Agent") String userAgent) throws JsonProcessingException {
//        RegisterCustomerDTO response = customerAuthService.customerUpdate(registerDTO,userAgent);
//        return ResponseEntity.ok(response);
//    }

//    @PreAuthorize("hasAnyRole('ADMIN','SALE','CUSTOMER')")
//    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
//    @GetMapping(value = {"/customers/getOne/RegisterCustomer/{id}"})
//    public ResponseEntity<RegisterCustomerDTO> getOneRegisterCustomer(@PathVariable int id) throws JsonProcessingException {
//        RegisterCustomerDTO response = customerAuthService.getOneRegisterCustomer(id);
//        return ResponseEntity.ok(response);
//    }

}
