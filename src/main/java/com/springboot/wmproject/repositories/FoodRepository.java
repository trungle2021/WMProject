package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food,Integer> {
    @Query("select a from Food a where a.foodName=:name")
    List<Food> checkValidFoodName(String name);

}
