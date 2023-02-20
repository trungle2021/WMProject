package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.FoodDetailDTO;
import com.springboot.wmproject.DTO.MaterialDTO;
import com.springboot.wmproject.services.FoodDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foodDetail")
public class FoodDetailController {

    private FoodDetailService service;
    @Autowired

    public FoodDetailController(FoodDetailService service) {
        this.service = service;
    }

    @GetMapping("/byfood/{foodId}")
    public ResponseEntity<List<FoodDetailDTO>> getAllDetailByFoodId(@PathVariable int foodId)
    {
        return ResponseEntity.ok(service.getAllDetailByFoodId(foodId));
    }

    @GetMapping("/byorder/{orderId}")
    public ResponseEntity<List<FoodDetailDTO>> getAllDetailByOrderId(@PathVariable int orderId)
    {
        return ResponseEntity.ok(service.getAllDetailByFoodId(orderId));
    }
    @PostMapping("/create")
    public ResponseEntity<FoodDetailDTO> createFoodDetail(@RequestBody FoodDetailDTO foodDtDTO)
    {return  new ResponseEntity<>(service.createFoodDetail(foodDtDTO), HttpStatus.CREATED);}
    @PutMapping(value = "/update")
    public ResponseEntity<FoodDetailDTO> updateFood(@RequestBody FoodDetailDTO foodDetailDTO){
        return ResponseEntity.ok(service.updateFoodDetail(foodDetailDTO));
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteMaterial(@PathVariable int id){
        service.deleteFoodDetail(id);
        return ResponseEntity.ok("Deleted Food Detail Successfully");
    }
}
