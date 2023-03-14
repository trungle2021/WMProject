package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.FoodDTO;
import com.springboot.wmproject.DTO.FoodDetailDTO;
import com.springboot.wmproject.DTO.ServiceDTO;
import com.springboot.wmproject.DTO.ServiceDetailDTO;
import com.springboot.wmproject.services.Service_Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {
    private Service_Service service;
    @Autowired
    public ServiceController(Service_Service service) {
        this.service = service;
    }


    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZE','CUSTOMER','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<List<ServiceDTO>> getAll()
    {
        return ResponseEntity.ok(service.getAllService());
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/create")
    public ResponseEntity<ServiceDTO> create(@RequestBody ServiceDTO serviceDTO)
    {return  new ResponseEntity<>(service.createService(serviceDTO), HttpStatus.CREATED);}


    @PreAuthorize("hasAnyRole('ADMIN','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = "/update")
    public ResponseEntity<ServiceDTO> update(@RequestBody ServiceDTO serviceDTO){
        return ResponseEntity.ok(service.updateService(serviceDTO));
    }


    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteService(@PathVariable int id){
        service.deleteService(id);
        return ResponseEntity.ok("Deleted Service Successfully");
    }
}
