package com.springboot.wmproject.entities;

import jakarta.persistence.*;
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

    @Basic
    @Column(name = "avatar", nullable = true)
    private String avatar;
    @OneToMany(mappedBy = "customersByCustomerId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<Booking> bookings = new HashSet<>();
    @OneToMany(mappedBy = "customersByCustomerId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<CustomerAccounts> customerAccounts = new HashSet<>();
    @OneToMany(mappedBy = "customersByCustomerId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<Orders> orders = new HashSet<>();

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "role_id", referencedColumnName = "id",insertable = false,updatable = false)
//    private Roles rolesByRoleId;


}
