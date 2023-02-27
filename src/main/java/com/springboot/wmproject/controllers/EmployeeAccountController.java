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
@RequestMapping("api/employeeAccounts")
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

    @GetMapping(value = "/{empAccountId}")
    public ResponseEntity<EmployeeAccountDTO> getEmployeeAccountByAccountId(@PathVariable int empAccountId){
        return ResponseEntity.ok(employeeAccountService.getEmployeeAccountByEmployeeAccountId(empAccountId));
    }

    @GetMapping(value = "/employee/{name}")
    public ResponseEntity<List<EmployeeAccountDTO>> findByEmployeeName(@PathVariable String name){
        return ResponseEntity.ok(employeeAccountService.findByName(name));
    }

    @GetMapping(value = "/employee/type/{empType}")
    public ResponseEntity<List<EmployeeAccountDTO>> filterByEmpType(@PathVariable String empType){
        return ResponseEntity.ok(employeeAccountService.filterByEmpType(empType));
    }

//    @PostMapping(value = "/create")
//    public ResponseEntity<EmployeeAccountDTO> createEmployeeAccount(@RequestBody EmployeeAccountDTO employeeAccountDTO){
//        return new ResponseEntity<>(employeeAccountService.createEmployeeAccount(employeeAccountDTO), HttpStatus.CREATED);
//    }
    @PutMapping(value = "/update")
    public ResponseEntity<EmployeeAccountDTO> updateEmployeeAccount(@RequestBody EmployeeAccountDTO employeeAccountDTO){
        return ResponseEntity.ok(employeeAccountService.updateEmployeeAccount(employeeAccountDTO));
    }
    @DeleteMapping("/delete/{empAccountId}")
    public ResponseEntity<String> deleteEmployeeAccount(@PathVariable int empAccountId){
        employeeAccountService.deleteEmployeeAccount(empAccountId);
        return ResponseEntity.ok("Employee Account Deleted");
    }

}
