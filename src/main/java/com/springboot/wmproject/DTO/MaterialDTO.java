package com.springboot.wmproject.DTO;

import com.springboot.wmproject.entities.Food;
import lombok.Data;

@Data

public class MaterialDTO {
    private int id;
    private String materialCode;
    private String materialName;
    private String unit;
    private int foodId;
    private Double count;
//    private Food foodByMaterialId;
}
