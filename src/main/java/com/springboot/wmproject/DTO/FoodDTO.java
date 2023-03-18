package com.springboot.wmproject.DTO;


import com.springboot.wmproject.entities.FoodImages;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
public class FoodDTO {
    private int id;
    @NotEmpty
    private String foodName;

    private String foodType;
    @NotEmpty
    private String description;
    @Min(1)
    private Double price;
    private boolean isActive;

    private Set<MaterialDTO> materialsById;
    private Set<FoodImageDTO> foodImagesById;
//    private Set<FoodDetailDTO> foodDetailsDTOById;
}
