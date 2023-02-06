package com.springboot.wmproject.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customers {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @Basic
    @Column(name = "address", nullable = false, length = 100)
    private String address;
    @Basic
    @Column(name = "phone", nullable = false, length = 15)
    private String phone;
    @Basic
    @Column(name = "gender", nullable = true, length = 10)
    private String gender;
    @JsonManagedReference
    @OneToMany(mappedBy = "customers",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<Booking> bookings;
    @JsonManagedReference
    @OneToMany(mappedBy = "customers",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<CustomerAccounts> customerAccounts;
    @JsonManagedReference
    @OneToMany(mappedBy = "customers",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<Orders> orders;

}
