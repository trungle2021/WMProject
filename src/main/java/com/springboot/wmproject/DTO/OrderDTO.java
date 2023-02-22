package com.springboot.wmproject.DTO;

import com.springboot.wmproject.entities.*;
import lombok.Data;

import java.math.BigDecimal;
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

    private Integer table_amount;
    private Integer part_time_emp_amount;
    private Set<FoodDetailDTO> foodDetails;
    private Venues venues;
    private Employees employees;
    private OrganizeTeams organizeTeams;
    private Customers customers;


//    private Set<ServiceDetailsDTO> serviceDetails;

}
