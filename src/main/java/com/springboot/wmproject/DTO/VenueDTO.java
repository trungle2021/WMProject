package com.springboot.wmproject.DTO;

import com.springboot.wmproject.entities.Orders;
import lombok.Data;

@Data

public class VenueDTO {
    private int id;
    private String venueName;
    private Integer minPeople;
    private Integer maxPeople;
    private Double price;
//    private Set<OrderDTO> orderDTOS;
}
