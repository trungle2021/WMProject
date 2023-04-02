package com.springboot.wmproject.DTO;

import com.springboot.wmproject.entities.Food;
import com.springboot.wmproject.entities.Materials;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class MaterialDetailDTO {
    private int id;

    private Integer materialId;

    private Integer foodId;

    private String unit;

    private Integer count;
    private MaterialDTO materialsByMaterialId;

}
