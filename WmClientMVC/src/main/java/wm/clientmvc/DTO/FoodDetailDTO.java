package wm.clientmvc.DTO;

import lombok.Data;

@Data
public class FoodDetailDTO {
    private int id;

    private Integer orderId;

    private Integer foodId;

//    private Integer count;
//
//    private BigDecimal price;

//    private Orders ordersByOrderId;
    private FoodDTO foodByFoodId;

}
