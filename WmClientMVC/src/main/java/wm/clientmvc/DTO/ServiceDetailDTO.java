package wm.clientmvc.DTO;

import lombok.Data;
import wm.clientmvc.entities.Orders;
import wm.clientmvc.entities.Services;


@Data
public class ServiceDetailDTO {
    private int id;

    private Integer orderId;

    private Integer serviceId;
//
//    private Orders ordersByOrderId;
//
//    private Services servicesByServiceId;
}
