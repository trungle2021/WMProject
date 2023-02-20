package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.FoodDTO;
import com.springboot.wmproject.DTO.FoodDetailDTO;
import com.springboot.wmproject.DTO.MaterialDTO;

import java.util.List;

public interface FoodDetailService {

    List<FoodDetailDTO> getAllDetailByFoodId(int foodId);
    List<FoodDetailDTO> getAllDetailByOrderId(Integer orderId);
//    FoodDetailDTO getOneMaterial(int materialId);
    FoodDetailDTO createFoodDetail(FoodDetailDTO newFoodDetailDTO);
    FoodDetailDTO updateFoodDetail(FoodDetailDTO uFoodDetailDTO);
    void deleteFoodDetail(int id);
}
