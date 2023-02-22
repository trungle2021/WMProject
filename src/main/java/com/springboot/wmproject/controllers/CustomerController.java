package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.CustomerDTO;
import com.springboot.wmproject.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    private CustomerService service;

    @Autowired
    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CustomerDTO>> getAllCustomer(){
        return new ResponseEntity<>(service.getAllCustomer(), HttpStatus.OK);
    }
}
