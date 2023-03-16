package com.springboot.wmproject.services.impl;


import com.springboot.wmproject.DTO.FoodDTO;
import com.springboot.wmproject.DTO.VenueDTO;
import com.springboot.wmproject.entities.Food;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.repositories.FoodRepository;
import com.springboot.wmproject.services.FoodService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService {
    private FoodRepository foodRepository;
    private ModelMapper modelMapper;
    @Autowired
    public FoodServiceImpl(FoodRepository foodRepository, ModelMapper modelMapper) {
        this.foodRepository = foodRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<FoodDTO> getAllFood() {
    return foodRepository.findAll().stream().map(food->mapToDTO(food)).collect(Collectors.toList());
    }

    @Override
    public List<FoodDTO> getAllFoodActive() {
        List<FoodDTO>foods= foodRepository.findAll().stream().map(food -> mapToDTO(food)).collect(Collectors.toList());
        List<FoodDTO> newList=new ArrayList<>();
        for (FoodDTO food:foods)
        {
            if(food.isActive())
            {
                newList.add(food);
            }
        }
        return newList;
    }


    @Override
    public FoodDTO getOneFood(int foodId) {
        return mapToDTO(foodRepository.findById(foodId).orElseThrow(()->new ResourceNotFoundException("food","id",String.valueOf(foodId))));

    }

    @Override
    public FoodDTO createFood(FoodDTO newFoodDTO) {


                //convert DTO to entity
                Food food = mapToEntity(newFoodDTO);


                //save booking
                Food newFood = foodRepository.save(food);

                FoodDTO foodResponse = mapToDTO(newFood);
                return foodResponse;


    }

    @Override
    public FoodDTO updateFood(FoodDTO editFoodDTO) {
        Food editFood= foodRepository.findById(editFoodDTO.getId()).orElseThrow(()->new ResourceNotFoundException("food","id",String.valueOf(editFoodDTO.getId())));
      if(editFood !=null)
      {
          editFood.setFoodName(editFoodDTO.getFoodName());
          editFood.setFoodType(editFoodDTO.getFoodType());
          editFood.setDescription(editFoodDTO.getDescription());
          editFood.setPrice(editFoodDTO.getPrice());
         Food updateFood= foodRepository.save(editFood);
         return mapToDTO(updateFood);
      }
        return null;
    }

    @Override
    public void deleteFood(int foodId) {
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new ResourceNotFoundException("Food","id",String.valueOf(foodId)));
        foodRepository.delete(food);
    }
    public FoodDTO mapToDTO(Food food){
        FoodDTO foodDTO = modelMapper.map(food, FoodDTO.class);
        foodDTO.setActive(food.isActive());
        return foodDTO;
    }

    public Food mapToEntity(FoodDTO foodDTO){
        Food food = modelMapper.map(foodDTO,Food.class);
        food.setActive(foodDTO.isActive());
        return food;
    }
}
