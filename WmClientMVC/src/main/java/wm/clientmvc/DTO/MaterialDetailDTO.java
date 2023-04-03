package wm.clientmvc.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class MaterialDetailDTO {
    private int id;

    private Integer materialId;

    private Integer foodId;
    @Min(0)
    @Max(50)
    @NotEmpty
    private Double count;

    private MaterialDTO materialsByMaterialId;
}
