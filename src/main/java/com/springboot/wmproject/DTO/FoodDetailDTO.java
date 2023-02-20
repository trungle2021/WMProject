package com.springboot.wmproject.DTO;

import com.springboot.wmproject.entities.Food;
import com.springboot.wmproject.entities.Orders;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
@Data
public class FoodDetailDTO {
    private int id;

    private Integer orderId;

    private Integer foodId;

    private Integer count;

    private BigDecimal price;

    private Orders ordersByOrderId;
    private Food foodByFoodId;

}
