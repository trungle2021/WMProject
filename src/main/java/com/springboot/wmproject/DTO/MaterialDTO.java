package com.springboot.wmproject.DTO;

import com.springboot.wmproject.entities.Food;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MaterialDTO {
    private int id;
    private String materialCode;
    private String materialName;
    private Double price;
    @NotEmpty
    @Size(min=5,max=45,message="unit max 45")
    private String unit;

//    private Food foodByMaterialId;
}
