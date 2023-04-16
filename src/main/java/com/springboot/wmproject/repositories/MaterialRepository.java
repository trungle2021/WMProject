package com.springboot.wmproject.repositories;

import com.springboot.wmproject.DTO.MaterialDTO;
import com.springboot.wmproject.entities.Food;
import com.springboot.wmproject.entities.Materials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MaterialRepository extends JpaRepository<Materials,Integer> {
    @Query("select a from Materials a where a.materialCode=:code")
    List<Materials> checkValidMaterialCode(String code);

}
