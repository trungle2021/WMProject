package wm.clientmvc.DTO;


import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Data
public class ServiceDTO {

    private int id;
    @NotEmpty
    @Size(min = 1, max = 45, message = "Name must be between 1 and 45 characters long")
    private String serviceName;
    @NotNull
    @PositiveOrZero(message = "Price must be positive or zero")
    @DecimalMax(value = "999.99", message = "Price must be at most 999,99")
    private Double price;
    @NotEmpty
    private String description;
    private boolean isActive;

//    private Set<ServiceDetailDTO> serviceDetailsById;
}
