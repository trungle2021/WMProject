package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.BookingDTO;
import com.springboot.wmproject.DTO.FoodDTO;
import com.springboot.wmproject.services.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodController {

    private FoodService foodService;
@Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }


    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/all")
    public ResponseEntity<List<FoodDTO>> getAll()
            {
        return ResponseEntity.ok(foodService.getAllFood());
    }
    @PreAuthorize("hasAnyRole('ADMIN','SALE','CUSTOMER','ORGANIZE','ANONYMOUS')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/allactive")
    public ResponseEntity<List<FoodDTO>> getAllActive()
    {
        return ResponseEntity.ok(foodService.getAllFoodActive());
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/create")
    public ResponseEntity<FoodDTO> createFood(@RequestBody FoodDTO foodDTO)
    {return  new ResponseEntity<>(foodService.createFood(foodDTO),HttpStatus.CREATED);}

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = "/update")
    public ResponseEntity<FoodDTO> updateFood(@RequestBody FoodDTO foodDTO){
        return ResponseEntity.ok(foodService.updateFood(foodDTO));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteFood(@PathVariable int id){
        foodService.deleteFood(id);
        return ResponseEntity.ok("Deleted Food Successfully");
    }
}
