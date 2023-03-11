package com.springboot.wmproject.DTO;

import com.springboot.wmproject.entities.Orders;
import com.springboot.wmproject.entities.Services;
import lombok.Data;



@Data
public class ServiceDetailDTO {
    private int id;

    private Integer orderId;

    private Integer serviceId;

//    private Orders ordersByOrderId;
//
//    private Services servicesByServiceId;
    private ServiceDTO servicesByServiceId;
}
