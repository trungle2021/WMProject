package wm.clientmvc.DTO;


import lombok.Data;

import java.util.Set;

@Data
public class FoodDTO {
    private int id;

    private String foodName;

    private String foodType;

    private String description;
    private boolean isActive;
    private Double price;
    private Set<MaterialDTO> materialsById;
    private Set<FoodImageDTO> foodImagesById;

//    private Set<FoodDetailDTO> foodDetailsDTOById;
}
