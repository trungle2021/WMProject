package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.FoodDTO;
import com.springboot.wmproject.DTO.VenueDTO;

import java.util.List;

public interface FoodService {

    List<FoodDTO> getAllFood();

    List<FoodDTO> getAllFoodActive();
    FoodDTO getOneFood(int foodId);
    FoodDTO createFood(FoodDTO newFoodDTO);
    FoodDTO updateFood(FoodDTO editFoodDTO);
    void deleteFood(int foodId);
}
