package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.FoodDetailDTO;
import com.springboot.wmproject.DTO.MaterialDTO;
import com.springboot.wmproject.services.FoodDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/byfood/{foodId}")
    public ResponseEntity<List<FoodDetailDTO>> getAllDetailByFoodId(@PathVariable int foodId)
    {
        return ResponseEntity.ok(service.getAllDetailByFoodId(foodId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/byorder/{orderId}")
    public ResponseEntity<List<FoodDetailDTO>> getAllDetailByOrderId(@PathVariable int orderId)
    {
        return ResponseEntity.ok(service.getAllDetailByFoodId(orderId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/create")
    public ResponseEntity<FoodDetailDTO> createFoodDetail(@RequestBody FoodDetailDTO foodDtDTO)
    {return  new ResponseEntity<>(service.createFoodDetail(foodDtDTO), HttpStatus.CREATED);}

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = "/update")
    public ResponseEntity<FoodDetailDTO> updateFood(@RequestBody FoodDetailDTO foodDetailDTO){
        return ResponseEntity.ok(service.updateFoodDetail(foodDetailDTO));
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteMaterial(@PathVariable int id){
        service.deleteFoodDetail(id);
        return ResponseEntity.ok("Deleted Food Detail Successfully");
    }
}
