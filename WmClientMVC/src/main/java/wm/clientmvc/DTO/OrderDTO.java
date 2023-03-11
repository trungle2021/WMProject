package wm.clientmvc.DTO;

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

    private Integer tableAmount;
    private Integer partTimeEmpAmount;

    //    private Set<FoodDetailDTO> foodDetails;
    private VenueDTO venues;
    private EmployeeDTO employeesByBookingEmp;
    private OrganizeTeamDTO organizeTeamsByOrganizeTeam;
    private Collection<FoodDetailDTO> foodDetailsById;
    private Collection<ServiceDetailDTO> serviceDetailsById;
//

//    private Set<ServiceDetailsDTO> serviceDetails;

}
