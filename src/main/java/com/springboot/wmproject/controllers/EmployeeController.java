package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.EmployeeDTO;
import com.springboot.wmproject.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/employee")
public class EmployeeController {
    private EmployeeService employeeService;
    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @GetMapping(value = "/all")
    public ResponseEntity<List<EmployeeDTO>> getAll(){
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }
    @GetMapping(value = "/search/type/{empType}")
    public ResponseEntity<List<EmployeeDTO>> searchByEmpType(@PathVariable String empType){
        return ResponseEntity.ok(employeeService.findAllByEmpType(empType));
    }
    @GetMapping(value = "/search/name/{name}")
    public ResponseEntity<List<EmployeeDTO>> searchByName(@PathVariable String name){
        return ResponseEntity.ok(employeeService.findAllByName(name));
    }
    @GetMapping(value = "/one/{id}")
    public ResponseEntity<EmployeeDTO> getOneById(@PathVariable int id){
        return ResponseEntity.ok(employeeService.getOneEmployeeById(id));
    }
    @PostMapping(value = "/create")
    public ResponseEntity<EmployeeDTO> creatEmployee(@RequestBody EmployeeDTO employeeDTO){
        return new ResponseEntity<>(employeeService.createEmployee(employeeDTO), HttpStatus.CREATED);
    }
    @PutMapping(value = "/update")
    public ResponseEntity<EmployeeDTO> updateEmployee(@RequestBody EmployeeDTO employeeDTO){
        return ResponseEntity.ok(employeeService.updateEmployee(employeeDTO));
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable int id){
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Employee has been deleted");
    }
}
