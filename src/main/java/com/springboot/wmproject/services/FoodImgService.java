package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.FoodImageDTO;
import com.springboot.wmproject.DTO.VenueImgDTO;

import java.util.List;

public interface FoodImgService {
    List<FoodImageDTO> getAllFoodImg();
    List<FoodImageDTO> getAllFoodImgByFoodId(int id);
    FoodImageDTO getOneFoodImgById(int id);
    List<FoodImageDTO> createMultipleFoodImg(List<FoodImageDTO> foodImageDTOS);
    FoodImageDTO updateFoodImg(FoodImageDTO foodImageDTO);
    void deleteFoodImg(int id);
}
