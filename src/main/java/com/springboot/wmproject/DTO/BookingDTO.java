package com.springboot.wmproject.DTO;

import com.springboot.wmproject.entities.Customers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data

public class BookingDTO {
    private int id;
    private Integer customerId;
    private String bookingDate;
    private String appointmentDate;
}

