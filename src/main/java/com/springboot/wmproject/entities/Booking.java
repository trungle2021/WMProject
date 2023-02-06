package com.springboot.wmproject.entities;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Booking {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "customer_id", nullable = true)
    private Integer customerId;
    @Basic
    @Column(name = "booking_date", nullable = true, length = 45)
    private String bookingDate;
    @Basic
    @Column(name = "appointment_date", nullable = true, length = 45)
    private String appointmentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id",nullable = false,insertable = false,updatable = false)
    private Customers customers;

}
