package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.Materials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Materials,Integer> {
    @Query("SELECT c FROM Materials c where c.foodId =:foodId")
    List<Materials> getAllMaterialByFoodId(Integer foodId);

}
