package wm.clientmvc.DTO;


import lombok.Data;

import java.util.Set;

@Data
public class FoodDTO {
    private int id;

    private String foodName;

    private String foodType;

    private String description;

    private Double price;


//    private Set<FoodDetailDTO> foodDetailsDTOById;
}
