package com.springboot.wmproject.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Orders {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "order_date", nullable = true, length = 45)
    private String orderDate;
    @Basic
    @Column(name = "order_status", nullable = true, length = 45)
    private String orderStatus;
    @Basic
    @Column(name = "order_total", nullable = true, precision = 2)
    private BigDecimal orderTotal;
    @Basic
    @Column(name = "time_happen", nullable = true, length = 20)
    private String timeHappen;
    @Basic
    @Column(name = "venue_id", nullable = true)
    private Integer venueId;
    @Basic
    @Column(name = "booking_emp", nullable = true)
    private Integer bookingEmp;
    @Basic
    @Column(name = "organize_team", nullable = true)
    private Integer organizeTeam;
    @Basic
    @Column(name = "customer_id", nullable = true)
    private Integer customerId;
    @JsonManagedReference
    @OneToMany(mappedBy = "ordersByOrderId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<FoodDetails> foodDetails;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "venue_id", referencedColumnName = "id",nullable = false,insertable = false,updatable = false)
    private Venues venues;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "booking_emp", referencedColumnName = "id",nullable = false,insertable = false,updatable = false)
    private Employees employees;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "organize_team", referencedColumnName = "id",nullable = false,insertable = false,updatable = false)
    private OrganizeTeams organizeTeams;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id",nullable = false,insertable = false,updatable = false)
    private Customers customers;
    @JsonManagedReference
    @OneToMany(mappedBy = "orders",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<ServiceDetails> serviceDetails;


}
