package wm.clientmvc.DTO;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class FoodDTO {
    private int id;
    @NotEmpty
    @Size(max=100)
    private String foodName;

    private String foodType;
    @NotEmpty
    @Size(min=1,max=250,message = "Must not empty and shorter than 250!")
    private String description;
    private boolean active;
    @Min(0)
    @Max(2000)
    private Double price;
    private Set<MaterialDetailDTO> materialDetailById;
    private Set<FoodImageDTO> foodImagesById;

//    private Set<FoodDetailDTO> foodDetailsDTOById;
}
