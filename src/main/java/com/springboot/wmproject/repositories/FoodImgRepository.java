package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.FoodImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FoodImgRepository extends JpaRepository<FoodImages,Integer> {
    @Query("select a from FoodImages a where a.foodId=:id")
    List<FoodImages> getAllFoodImgByFoodId(int id);
}
