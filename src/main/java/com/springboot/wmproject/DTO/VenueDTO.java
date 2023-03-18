package com.springboot.wmproject.DTO;

import com.springboot.wmproject.entities.Orders;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.bind.annotation.Mapping;

import java.util.List;

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


    private boolean isActive;
    private List<VenueImgDTO> venueImagesById;


}
