package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.BookingDTO;
import com.springboot.wmproject.DTO.FoodDTO;
import com.springboot.wmproject.services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @GetMapping
    public ResponseEntity<List<FoodDTO>> getAll()
            {
        return ResponseEntity.ok(foodService.getAllFood());
    }
    @PostMapping("/create")
    public ResponseEntity<FoodDTO> createFood(@RequestBody FoodDTO foodDTO)
    {return  new ResponseEntity<>(foodService.createFood(foodDTO),HttpStatus.CREATED);}
    @PutMapping(value = "/update")
    public ResponseEntity<FoodDTO> updateFood(@RequestBody FoodDTO foodDTO){
        return ResponseEntity.ok(foodService.updateFood(foodDTO));
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteFood(@PathVariable int id){
        foodService.deleteFood(id);
        return ResponseEntity.ok("Deleted Food Successfully");
    }
}
