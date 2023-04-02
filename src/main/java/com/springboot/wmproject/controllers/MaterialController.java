package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.FoodDTO;
import com.springboot.wmproject.DTO.MaterialDTO;
import com.springboot.wmproject.DTO.ServiceDTO;
import com.springboot.wmproject.services.FoodService;
import com.springboot.wmproject.services.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private MaterialService materialService;

    @Autowired
    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','SALE','CUSTOMER','ORGANIZE','ANONYMOUS')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/all")
    public ResponseEntity<List<MaterialDTO>> getAll() {
        return ResponseEntity.ok(materialService.getAllMaterial());
    }

    @PreAuthorize("hasAnyRole('ADMIN','ANONYMOUS')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/create")
    public ResponseEntity<MaterialDTO> createFood(@RequestBody MaterialDTO materialDTO) {
        return new ResponseEntity<>(materialService.createMaterial(materialDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = "/update")
    public ResponseEntity<MaterialDTO> updateFood(@RequestBody MaterialDTO materialDTO) {
        return ResponseEntity.ok(materialService.updateMaterial(materialDTO));
    }
    @PreAuthorize("hasAnyRole('ADMIN','SALE','ANONYMOUS')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("getOne/{id}")
    public ResponseEntity<MaterialDTO> getOne(@PathVariable int id)
    {
        return ResponseEntity.ok(materialService.getOneMaterial(id));
    }
}
