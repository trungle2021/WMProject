package wm.clientmvc.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

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
    private boolean active;
//    private Set<OrderDTO> orderDTOS;
    private List<VenueImgDTO> venueImagesById;
}
