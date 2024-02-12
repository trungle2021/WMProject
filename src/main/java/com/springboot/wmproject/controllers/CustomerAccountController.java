package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.CustomerAccountDTO;
import com.springboot.wmproject.components.Customer.CustomerAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/customerAccounts")
public class CustomerAccountController {
    private CustomerAccountService customerAccountService;

    @Autowired
    public CustomerAccountController(CustomerAccountService customerAccountService) {
        this.customerAccountService = customerAccountService;
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasAnyRole('ADMIN','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<CustomerAccountDTO>> getAll(){
        return ResponseEntity.ok(customerAccountService.findAll());
    }
    @PreAuthorize("hasAnyRole('ADMIN','SALE','CUSTOMER')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/customer/{id}")
    public ResponseEntity<CustomerAccountDTO> getCustomerAccountByCustomerId(@PathVariable int id){
        return ResponseEntity.ok(customerAccountService.getAccountByCustomerId(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','SALE','CUSTOMER')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/{id}")
    public ResponseEntity<CustomerAccountDTO> getCustomerAccountByAccountId(@PathVariable int id){
        return ResponseEntity.ok(customerAccountService.getAccountByAccountId(id));
    }

    @PutMapping(value = "/update")
    @PreAuthorize("hasAnyRole('ADMIN','SALE','CUSTOMER')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CustomerAccountDTO> update(@RequestBody CustomerAccountDTO customerAccountDTO){
        return ResponseEntity.ok(customerAccountService.update(customerAccountDTO));
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> delete(@PathVariable int id){
        customerAccountService.delete(id);
        return ResponseEntity.ok("Customer Account Deleted");
    }


}
