package com.springboot.wmproject.DTO;

import com.springboot.wmproject.entities.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.util.Collection;
import java.util.Set;

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
