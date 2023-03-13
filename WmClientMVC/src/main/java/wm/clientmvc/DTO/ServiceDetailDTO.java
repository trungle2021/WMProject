package wm.clientmvc.DTO;

import lombok.Data;


@Data
public class ServiceDetailDTO {
    private int id;

    private Integer orderId;

    private Integer serviceId;
//
//    private Orders ordersByOrderId;
//
    private ServiceDTO servicesByServiceId;
}
