package wm.clientmvc.DTO;

import lombok.Data;

@Data
public class MaterialDetailDTO {
    private int id;

    private Integer materialId;

    private Integer foodId;

    private Integer count;

    private MaterialDTO materialsByMaterialId;
}
