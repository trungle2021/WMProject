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
    private String description;
    private boolean active;
    @Min(1)
    private Double price;
    private Set<MaterialDTO> materialsById;
    private Set<FoodImageDTO> foodImagesById;

//    private Set<FoodDetailDTO> foodDetailsDTOById;
}
