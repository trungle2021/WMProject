package com.springboot.wmproject.DTO;

import com.springboot.wmproject.entities.Orders;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data

public class VenueDTO {

    private int id;
    @NotEmpty
    private String venueName;
    @Min(1)
    @Max(3000)
    private Integer minPeople;
    @Min(1)
    @Max(3000)
    private Integer maxPeople;
    @Min(1)
    private Double price;
//    private Set<OrderDTO> orderDTOS;
}
