package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.FoodDTO;
import com.springboot.wmproject.DTO.ServiceDetailDTO;
import com.springboot.wmproject.services.ServiceDetail_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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



    @GetMapping("/byorder/{orderId}")
    public ResponseEntity<List<ServiceDetailDTO>> getAllDetailByOrder(@PathVariable int orderId)
    {
        return ResponseEntity.ok(service.getAllDetailByOrder(orderId));
    }
    @GetMapping("/byservice/{serviceId}")
    public ResponseEntity<List<ServiceDetailDTO>> getAllDetailByService(@PathVariable int serviceId)
    {
        return ResponseEntity.ok(service.getAllDetailByService(serviceId));
    }

    @PostMapping("/create")
    public ResponseEntity<ServiceDetailDTO> createDetail(@RequestBody ServiceDetailDTO serviceDetailDTO)
    {return  new ResponseEntity<>(service.createDetail(serviceDetailDTO), HttpStatus.CREATED);}


    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteFood(@PathVariable int id){
        service.deleteDetail(id);
        return ResponseEntity.ok("Deleted Food Successfully");
    }
}
