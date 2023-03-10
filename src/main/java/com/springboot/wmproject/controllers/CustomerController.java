package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.CustomerDTO;
import com.springboot.wmproject.services.AuthServices.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/customers")
public class CustomerController {
    private CustomerService service;

    @Autowired
    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/all")
    public ResponseEntity<List<CustomerDTO>> getAllCustomer(){
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable int id){
        return new ResponseEntity<CustomerDTO>(service.getCustomerById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/update")
    public ResponseEntity<CustomerDTO> update(@RequestBody CustomerDTO customerDTO){
        return new ResponseEntity<CustomerDTO>(service.update(customerDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("delete/{id}")
    public ResponseEntity<String> delete(@PathVariable int id){
        service.delete(id);
        return ResponseEntity.ok("Deleted Customer Successfully");
    }

}
