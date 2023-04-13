package wm.clientmvc.DTO;

import lombok.Data;

@Data

public class MaterialDTO {
    private int id;
    private String materialCode;
    private String materialName;
    private String unit;
    private int foodId;

    private Double count;
//    private Food foodByMaterialId;
}
