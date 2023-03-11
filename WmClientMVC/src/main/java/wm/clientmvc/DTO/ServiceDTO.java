package wm.clientmvc.DTO;

import wm.clientmvc.entities.ServiceDetails;

import lombok.Data;

import java.util.Set;
@Data
public class ServiceDTO {
    private int id;

    private String serviceName;

    private Double price;

    private String description;

//    private Set<ServiceDetailDTO> serviceDetailsById;
}
