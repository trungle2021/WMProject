package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.FoodDetailDTO;
import com.springboot.wmproject.DTO.MaterialDetailDTO;
import com.springboot.wmproject.exceptions.WmAPIException;
import com.springboot.wmproject.services.MaterialDetailService;
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
@RequestMapping("/api/materialDetails")
public class MaterialDetailController {

MaterialDetailService service;
    @Autowired
    public MaterialDetailController(MaterialDetailService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('ADMIN','SALE','CUSTOMER','ORGANIZE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/byfood/{foodId}")
    public ResponseEntity<List<MaterialDetailDTO>> getAllDetailByFoodId(@PathVariable int foodId)
    {
        return ResponseEntity.ok(service.getAllDetailByFoodId(foodId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SALE','CUSTOMER','ORGANIZE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/byMaterial/{materialId}")
    public ResponseEntity<List<MaterialDetailDTO>> getAllDetailByMaterialId(@PathVariable int materialId)
    {
        return ResponseEntity.ok(service.getAllDetailByMaterialId(materialId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SALE','CUSTOMER','ORGANIZE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/byOrder/{orderId}")
    public ResponseEntity<List<MaterialDetailDTO>> getAllDetailByOrderId(@PathVariable int orderId)
    {
        return ResponseEntity.ok(service.getAllMaterialByOrder(orderId));
    }
    @PreAuthorize("hasAnyRole('ADMIN','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/create")
    public ResponseEntity<MaterialDetailDTO> createMaterialDetail(@RequestBody MaterialDetailDTO detailDTO)
    {return  new ResponseEntity<>(service.createMaterialDetail(detailDTO), HttpStatus.CREATED);}

    @PreAuthorize("hasAnyRole('ADMIN','SALE','CUSTOMER')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = "/update")
    public ResponseEntity<MaterialDetailDTO> updateMaterial(@RequestBody MaterialDetailDTO detailDTO){
        return ResponseEntity.ok(service.updateMaterialDetail(detailDTO));
    }
    @PreAuthorize("hasAnyRole('ADMIN','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody String materialIdList){


        String[] arr = materialIdList.split(",");
        if(arr!=null && arr.length!=0) {
            for (int i = 0; i < arr.length; i++) {
                service.deleteMaterialDetail(Integer.parseInt(arr[i]));
            }
            return ResponseEntity.ok("Remove material success");
        }else {
            return null;
        }
    }




}
