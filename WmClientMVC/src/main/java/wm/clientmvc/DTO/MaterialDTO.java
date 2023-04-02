package wm.clientmvc.DTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Data
public class MaterialDTO {
    private int id;
    //unique here
    @NotEmpty
    private String materialCode;
    @NotEmpty
    @Size(min=5,max=45,message="Name from 5 to 45")
    private String materialName;

    @NotEmpty
    @Size(max=45,message="unit max 45")
    private String unit;
    @DecimalMax(value = "5000", message = "Price must be at most 5000")
    private Double price;
//    private Food foodByMaterialId;
}
