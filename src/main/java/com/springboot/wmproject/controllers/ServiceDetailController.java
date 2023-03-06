package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.FoodDTO;
import com.springboot.wmproject.DTO.ServiceDetailDTO;
import com.springboot.wmproject.services.ServiceDetail_Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicedetail")
public class ServiceDetailController {

    ServiceDetail_Service service;
    @Autowired

    public ServiceDetailController(ServiceDetail_Service service) {
        this.service = service;
    }



    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/byorder/{orderId}")
    public ResponseEntity<List<ServiceDetailDTO>> getAllDetailByOrder(@PathVariable int orderId)
    {
        return ResponseEntity.ok(service.getAllDetailByOrder(orderId));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/byservice/{serviceId}")
    public ResponseEntity<List<ServiceDetailDTO>> getAllDetailByService(@PathVariable int serviceId)
    {
        return ResponseEntity.ok(service.getAllDetailByService(serviceId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/create")
    public ResponseEntity<ServiceDetailDTO> createDetail(@RequestBody ServiceDetailDTO serviceDetailDTO)
    {return  new ResponseEntity<>(service.createDetail(serviceDetailDTO), HttpStatus.CREATED);}


    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteFood(@PathVariable int id){
        service.deleteDetail(id);
        return ResponseEntity.ok("Deleted Food Successfully");
    }
}
