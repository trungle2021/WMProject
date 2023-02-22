package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.FoodDTO;
import com.springboot.wmproject.DTO.ServiceDTO;
import com.springboot.wmproject.services.Service_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service")
public class ServiceController {
    private Service_Service service;
    @Autowired
    public ServiceController(Service_Service service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ServiceDTO>> getAll()
    {
        return ResponseEntity.ok(service.getAllService());
    }
    @PostMapping("/create")
    public ResponseEntity<ServiceDTO> create(@RequestBody ServiceDTO serviceDTO)
    {return  new ResponseEntity<>(service.createService(serviceDTO), HttpStatus.CREATED);}
    @PutMapping(value = "/update")
    public ResponseEntity<ServiceDTO> update(@RequestBody ServiceDTO serviceDTO){
        return ResponseEntity.ok(service.updateService(serviceDTO));
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteService(@PathVariable int id){
        service.deleteService(id);
        return ResponseEntity.ok("Deleted Service Successfully");
    }
}
