package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.FoodDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodDetailRepository extends JpaRepository<FoodDetails,Integer> {
    @Query("SELECT c FROM  FoodDetails c where c.foodId =:foodId")
    List<FoodDetails> getAllDetailByFoodId(Integer foodId);
    @Query("SELECT c FROM  FoodDetails c where c.orderId =:orderId")
    List<FoodDetails> getAllDetailByOrderId(Integer orderId);
    @Modifying
    @Query("delete from FoodDetails c where c.orderId =:orderId")
    void deleteByOrderId(@Param("orderId") Integer orderId);


}
