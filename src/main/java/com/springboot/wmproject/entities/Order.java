package com.springboot.wmproject.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "order_date", length = 45)
    private String orderDate;

    @Column(name = "order_status", length = 45)
    private String orderStatus;

    @Column(name = "order_total", precision = 15, scale = 2)
    private Double orderTotal;

    @Column(name = "time_happen", length = 20)
    private String timeHappen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venueId")
    private Venue venue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_emp")
    private Employee bookingEmp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organize_team")
    private OrganizeTeam organizeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId")
    private Customer customer;



}