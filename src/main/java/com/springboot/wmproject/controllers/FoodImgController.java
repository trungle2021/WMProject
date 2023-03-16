package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.FoodImageDTO;
import com.springboot.wmproject.DTO.VenueImgDTO;
import com.springboot.wmproject.services.FoodImgService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foodImgs")
public class FoodImgController {
    private FoodImgService foodImgService;
    @Autowired
    public FoodImgController(FoodImgService foodImgService) {
        this.foodImgService = foodImgService;
    }
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZE','CUSTOMER','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = {"/all"})
    public ResponseEntity<List<FoodImageDTO>> getAll() {
        return ResponseEntity.ok(foodImgService.getAllFoodImg());
    }
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZE','CUSTOMER','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = {"/one/{id}"})
    public ResponseEntity<FoodImageDTO> getOneById(@PathVariable int id) {
        return ResponseEntity.ok(foodImgService.getOneFoodImgById(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZE','CUSTOMER','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = {"/all/food/{id}"})
    public ResponseEntity<List<FoodImageDTO>> getAllByFoodId(@PathVariable int id) {
        return ResponseEntity.ok(foodImgService.getAllFoodImgByFoodId(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/creates")
    public ResponseEntity<List<FoodImageDTO>> createMultipleVenueImg(@Valid @RequestBody List<FoodImageDTO> foodImageDTOS) {
        return new ResponseEntity<>(foodImgService.createMultipleFoodImg(foodImageDTOS), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = "/update")
    public ResponseEntity<FoodImageDTO> updateVenueImg(@Valid @RequestBody FoodImageDTO foodImageDTO) {
        return ResponseEntity.ok(foodImgService.updateFoodImg(foodImageDTO));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteVenueImg(@Valid @PathVariable int id) {
        foodImgService.deleteFoodImg(id);
        return ResponseEntity.ok("Venue image has been deleted");
    }
}
