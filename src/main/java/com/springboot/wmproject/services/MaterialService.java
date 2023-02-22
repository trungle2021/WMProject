package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.FoodDTO;
import com.springboot.wmproject.DTO.MaterialDTO;

import java.util.List;

public interface MaterialService {
    List<MaterialDTO> getAllMaterialByFoodId(Integer foodId);
    List<MaterialDTO> getAllMaterial();
    MaterialDTO getOneMaterial(int materialId);
    MaterialDTO createMaterial(MaterialDTO newMaterialDTO);
    MaterialDTO updateMaterial(MaterialDTO editMaterialDTO);
    void deleteMaterial(int MaterialId);
}
