package com.springboot.wmproject.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.Objects;
@Getter
@Setter
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
    private Double orderTotal;
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
    @Basic
    @Column(name = "table_amount", nullable = true)
    private Integer tableAmount;
    @Basic
    @Column(name = "part_time_emp_amount", nullable = true)
    private Integer partTimeEmpAmount;

    @Basic
    @Column(name = "cost", nullable = true, precision = 2)

    private Double cost;
    @Basic
    @Column(name = "contract", nullable = true, length = -1)
    private String contract;
    @OneToMany(mappedBy = "ordersByOrderId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<FoodDetails> foodDetailsById;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", referencedColumnName = "id",insertable = false,updatable = false)
    private Venues venues;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_emp", referencedColumnName = "id",insertable = false,updatable = false)
    private Employees employeesByBookingEmp;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organize_team", referencedColumnName = "id",insertable = false,updatable = false)
    private OrganizeTeams organizeTeamsByOrganizeTeam;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id",insertable = false,updatable = false)
    private Customers customersByCustomerId;
    @OneToMany(mappedBy = "ordersByOrderId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<ServiceDetails> serviceDetailsById;


    public Orders(int id, String orderStatus, String timeHappen, Integer venueId) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.timeHappen = timeHappen;
        this.venueId = venueId;
    }

}
