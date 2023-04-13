package com.springboot.wmproject.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class VenueImgDTO {
    private int id;
    private String url;
    private int venueId;
}
