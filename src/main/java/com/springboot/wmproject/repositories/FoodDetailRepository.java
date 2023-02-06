package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.FoodDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodDetailRepository extends JpaRepository<FoodDetails,Integer> {
}
