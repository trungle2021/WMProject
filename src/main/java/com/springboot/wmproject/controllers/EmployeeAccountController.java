package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.EmployeeAccountDTO;
import com.springboot.wmproject.components.Employee.EmployeeAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/employeeAccounts")
public class EmployeeAccountController {
    private EmployeeAccountService employeeAccountService;

    @Autowired
    public EmployeeAccountController(EmployeeAccountService employeeAccountService) {
        this.employeeAccountService = employeeAccountService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/all")
    public ResponseEntity<List<EmployeeAccountDTO>> getAll(){
        return ResponseEntity.ok(employeeAccountService.getAllEmployeeAccounts());
    }

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZE','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/{empAccountId}")
    public ResponseEntity<EmployeeAccountDTO> getEmployeeAccountByAccountId(@PathVariable int empAccountId){
        return ResponseEntity.ok(employeeAccountService.getEmployeeAccountByEmployeeAccountId(empAccountId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/employee/{name}")
    public ResponseEntity<List<EmployeeAccountDTO>> findByEmployeeName(@PathVariable String name){
        return ResponseEntity.ok(employeeAccountService.findByName(name));
    }

    @PutMapping(value = "/update")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZE','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<EmployeeAccountDTO> updateEmployeeAccount(@RequestBody EmployeeAccountDTO employeeAccountDTO){
        return ResponseEntity.ok(employeeAccountService.update(employeeAccountDTO));
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> deleteEmployeeAccount(@PathVariable int id){
        employeeAccountService.delete(id);
        return ResponseEntity.ok("Employee Account Deleted");
    }

}
