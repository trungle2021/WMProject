package com.springboot.wmproject.DTO;

import com.springboot.wmproject.entities.Food;
import com.springboot.wmproject.entities.Orders;
import lombok.Data;

@Data
public class FoodDetailDTO {
    private int id;

    private Integer orderId;

    private Integer foodId;

//    private Integer count;
//
//    private BigDecimal price;


}
