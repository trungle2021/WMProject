package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.EmployeeAccountDTO;
import com.springboot.wmproject.entities.EmployeeAccounts;
import com.springboot.wmproject.services.EmployeeAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/employeeAccount")
public class EmployeeAccountController {
    private EmployeeAccountService employeeAccountService;

    @Autowired
    public EmployeeAccountController(EmployeeAccountService employeeAccountService) {
        this.employeeAccountService = employeeAccountService;
    }
    @GetMapping(value = "/all")
    public ResponseEntity<List<EmployeeAccountDTO>> getAll(){
        return ResponseEntity.ok(employeeAccountService.getAllEmployeeAccounts());
    }
    @PostMapping(value = "/create")
    public ResponseEntity<EmployeeAccountDTO> createEmployeeAccount(@RequestBody EmployeeAccountDTO employeeAccountDTO){
        return new ResponseEntity<>(employeeAccountService.createEmployeeAccount(employeeAccountDTO), HttpStatus.CREATED);
    }
    @PutMapping(value = "/update")
    public ResponseEntity<EmployeeAccountDTO> updateEmployeeAccount(@RequestBody EmployeeAccountDTO employeeAccountDTO){
        return ResponseEntity.ok(employeeAccountService.updateEmployeeAccount(employeeAccountDTO));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployeeAccount(@PathVariable int id){
        employeeAccountService.deleteEmployeeAccount(id);
        return ResponseEntity.ok("Account Employee Deleted");
    }

}
