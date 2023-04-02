package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FoodRepository extends JpaRepository<Food,Integer> {
    @Query("select a from Food a where a.foodName=:name")
    List<Food> checkValidFoodName(String name);

}
