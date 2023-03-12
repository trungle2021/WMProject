package wm.clientmvc.DTO;

import lombok.Data;

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

    private Integer table_amount;
    private Integer part_time_emp_amount;

    //    private Set<FoodDetailDTO> foodDetails;
    private VenueDTO venues;
//    private Employees employees;
//    private OrganizeTeams organizeTeams;
//    private Customers customers;
//

//    private Set<ServiceDetailsDTO> serviceDetails;

}
