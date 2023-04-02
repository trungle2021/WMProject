package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.FoodDetails;
import com.springboot.wmproject.entities.MaterialDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialDetailRepository extends JpaRepository<MaterialDetail,Integer> {

    @Query("SELECT c FROM  MaterialDetail c where c.foodId =:foodId")
    List<MaterialDetail> getAllDetailByFoodId(Integer foodId);
    @Query("SELECT c FROM  MaterialDetail c where c.materialId =:materialId")
    List<MaterialDetail> getAllDetailByMaterialId(Integer materialId);



}
