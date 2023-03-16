package com.springboot.wmproject.DTO;


import lombok.Data;

import java.util.Set;

@Data
public class FoodDTO {
    private int id;

    private String foodName;

    private String foodType;

    private String description;

    private Double price;
    private boolean isActive;

    private Set<MaterialDTO> materialsById;
//    private Set<FoodDetailDTO> foodDetailsDTOById;
}
