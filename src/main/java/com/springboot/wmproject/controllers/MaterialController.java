package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.FoodDTO;
import com.springboot.wmproject.DTO.MaterialDTO;
import com.springboot.wmproject.services.FoodService;
import com.springboot.wmproject.services.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/material")
public class MaterialController {

    private MaterialService materialService;
    @Autowired

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }
    @GetMapping
    public ResponseEntity<List<MaterialDTO>> getAll()
    {
        return ResponseEntity.ok(materialService.getAllMaterial());
    }

    @GetMapping("/{foodId}")
    public ResponseEntity<List<MaterialDTO>> getAllByFoodId(@PathVariable Integer foodId)
    {
        return ResponseEntity.ok(materialService.getAllMaterialByFoodId(foodId));
    }
    @PostMapping("/create")
    public ResponseEntity<MaterialDTO> createMaterial(@RequestBody MaterialDTO materialDTO)
    {return  new ResponseEntity<>(materialService.createMaterial(materialDTO), HttpStatus.CREATED);}
    @PutMapping(value = "/update")
    public ResponseEntity<MaterialDTO> updateFood(@RequestBody MaterialDTO materialDTO){
        return ResponseEntity.ok(materialService.updateMaterial(materialDTO));
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteMaterial(@PathVariable int id){
        materialService.deleteMaterial(id);
        return ResponseEntity.ok("Deleted Material Successfully");
    }
}
