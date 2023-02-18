package com.springboot.wmproject.DTO;

import lombok.Data;

@Data
public class OrderDTO {
    private int id;
    private String orderDate;
    private String orderStatus;
    private double orderTotal;
    private String timeHappen;
    private int venueId;
    private int bookingEmp;
    private int organizeTeam;
    private int customerId;
}
