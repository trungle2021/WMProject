package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.FoodDetailDTO;
import com.springboot.wmproject.DTO.MaterialDTO;
import com.springboot.wmproject.DTO.MaterialDetailDTO;
import com.springboot.wmproject.entities.MaterialDetail;

import java.util.List;

public interface MaterialDetailService {

    List<MaterialDetailDTO> getAllDetailByFoodId(int foodId);
    List<MaterialDetailDTO> getAllDetailByMaterialId(Integer materialId);
    //    FoodDetailDTO getOneMaterial(int materialId);
    public List<MaterialDetailDTO> getAllMaterialByOrder(Integer orderId);
    MaterialDetailDTO createMaterialDetail(MaterialDetailDTO newMaterialDetail);
    MaterialDetailDTO updateMaterialDetail(MaterialDetailDTO uMaterialDetail);
    void deleteMaterialDetail(int id);
}
