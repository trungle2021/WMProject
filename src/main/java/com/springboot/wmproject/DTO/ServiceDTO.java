package com.springboot.wmproject.DTO;

import com.springboot.wmproject.entities.ServiceDetails;
import lombok.Data;


import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;
@Data
public class ServiceDTO {
    private int id;

    private String serviceName;

    private Double price;

    private String description;

    private Set<ServiceDetails> serviceDetailsById;
}
