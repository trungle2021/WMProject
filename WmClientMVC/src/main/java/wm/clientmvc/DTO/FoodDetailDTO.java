package wm.clientmvc.DTO;

import lombok.Data;
import wm.clientmvc.entities.Food;
import wm.clientmvc.entities.Orders;

@Data
public class FoodDetailDTO {
    private int id;

    private Integer orderId;

    private Integer foodId;

//    private Integer count;
//
//    private BigDecimal price;

    private Orders ordersByOrderId;
    private Food foodByFoodId;

}
