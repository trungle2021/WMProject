package com.springboot.wmproject.components.Auth.Employee;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.wmproject.DTO.JWTAuthResponse;
import com.springboot.wmproject.components.Auth.dto.LoginDTO;
import com.springboot.wmproject.components.Auth.dto.RegisterDTO;
import com.springboot.wmproject.components.Auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@AllArgsConstructor
public class EmployeeAuthController {

    private final AuthService authService;

    @PostMapping(value = {"/employees/login"})
    public ResponseEntity<JWTAuthResponse> staffLogin(@RequestBody LoginDTO loginDTO){
        HashMap<String,String> map = authService.employeeLogin(loginDTO);

        if(map.containsKey("accessToken") && map.containsKey("refreshToken")){
            JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
            jwtAuthResponse.setAccessToken(map.get("accessToken"));
            jwtAuthResponse.setRefreshToken(map.get("refreshToken"));
            return ResponseEntity.ok(jwtAuthResponse);
        }
        return null;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = {"/employees/create"})
    public ResponseEntity<RegisterDTO> staffRegister(@RequestBody RegisterDTO registerDTO) throws JsonProcessingException {
        RegisterDTO response = authService.employeeRegister(registerDTO);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SALE','ORGANIZE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = {"/employees/update"})
    public ResponseEntity<RegisterDTO> staffUpdate(@RequestBody RegisterDTO registerDTO) throws JsonProcessingException {
        RegisterDTO response = authService.employeeUpdate(registerDTO);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(value = {"/employees/delete/{id}"})
    public ResponseEntity<String> staffDelete(@PathVariable int id) {
        String response = authService.staffDelete(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SALE','ORGANIZE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = {"/employees/findRoleByEmployeeID/{id}"})
    public ResponseEntity<String> findRoleByEmployeeID(@PathVariable int id) {
        String response = authService.findRoleByEmployeeID(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SALE','ORGANIZE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = {"/employees/getOne/RegisterEmployee/{id}"})
    public ResponseEntity<RegisterDTO> getOneRegisterEmp(@PathVariable int id) throws JsonProcessingException {
        RegisterDTO response = authService.getOneRegisterEmp(id);
        return ResponseEntity.ok(response);
    }
}
