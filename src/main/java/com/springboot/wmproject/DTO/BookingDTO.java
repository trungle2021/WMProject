package com.springboot.wmproject.DTO;

import com.springboot.wmproject.entities.Customers;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private int id;
    private Integer customerId;
    private String bookingDate;
    private String appointmentDate;
//    private Customers customers;
}

