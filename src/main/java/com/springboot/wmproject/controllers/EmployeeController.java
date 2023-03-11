package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.EmployeeDTO;
import com.springboot.wmproject.services.AuthServices.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/employees")
public class EmployeeController {
    private EmployeeService employeeService;
    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/all")
    public ResponseEntity<List<EmployeeDTO>> getAll(){
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

//    @PreAuthorize("hasAnyRole('ADMIN')")
//    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
//    @GetMapping(value = "/search/type/{empType}")
//    public ResponseEntity<List<EmployeeDTO>> searchByEmpType(@PathVariable String empType){
//        return ResponseEntity.ok(employeeService.findAllByEmpType(empType));
//    }

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/search/name/{name}")
    public ResponseEntity<List<EmployeeDTO>> searchByName(@PathVariable String name){
        return ResponseEntity.ok(employeeService.findAllByName(name));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SALE','ORGANIZE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable int id){
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SALE','ORGANIZE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = "/update")
    public ResponseEntity<EmployeeDTO> updateEmployee(@RequestBody EmployeeDTO employeeDTO){
        return ResponseEntity.ok(employeeService.update(employeeDTO));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable int id){
        employeeService.delete(id);
        return ResponseEntity.ok("Employee has been deleted");
    }
}
