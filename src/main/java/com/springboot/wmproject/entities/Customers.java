package com.springboot.wmproject.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers", schema = "wmproject")
public class Customers {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "first_name", length = 45)
    private String first_name;
    @Basic
    @Column(name = "last_name", length = 45)
    private String last_name;

    @Basic
    @Column(name = "address", length = 100)
    private String address;
    @Basic
    @Column(name = "phone",length = 15)
    private String phone;
    @Basic
    @Column(name = "gender", nullable = true, length = 10)
    private String gender;

    @Basic
    @Column(name = "email", length = 50)
    private String email;

    @Basic
    @Column(name = "avatar", nullable = true)
    private String avatar;
    @JsonIgnoreProperties({"bookings","customerAccounts","orders"})
    @OneToMany(mappedBy = "customersByCustomerId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<Booking> bookings = new HashSet<>();


    @JsonIgnoreProperties({"bookings","customerAccounts","orders"})
    @OneToMany(mappedBy = "customersByCustomerId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<CustomerAccounts> customerAccounts = new HashSet<>();



    @JsonIgnoreProperties({"bookings","customerAccounts","orders"})
    @OneToMany(mappedBy = "customersByCustomerId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<Orders> orders = new HashSet<>();


}
