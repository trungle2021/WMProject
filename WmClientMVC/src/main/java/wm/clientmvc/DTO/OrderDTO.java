package wm.clientmvc.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Collection;

@Data
public class OrderDTO {
    private int id;

    private String orderDate;
    private String orderStatus;

    private Double orderTotal;

    private String timeHappen;

    private Integer venueId;


    private Integer bookingEmp;

    private Integer organizeTeam;

    private Integer customerId;
    @NotNull(message = "Table amount is required")
    private Integer tableAmount;
    private Integer partTimeEmpAmount;
    private String contract;

    //    private Set<FoodDetailDTO> foodDetails;
    private VenueDTO venues;
    private EmployeeDTO employeesByBookingEmp;
    private OrganizeTeamDTO organizeTeamsByOrganizeTeam;
    private Collection<FoodDetailDTO> foodDetailsById;
    private Collection<ServiceDetailDTO> serviceDetailsById;
    private CustomerDTO customersByCustomerId;
//

//    private Set<ServiceDetailsDTO> serviceDetails;

}
