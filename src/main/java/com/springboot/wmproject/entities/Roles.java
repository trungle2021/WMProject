//package com.springboot.wmproject.entities;
//
//import jakarta.persistence.*;
//
//import java.util.Collection;
//import java.util.HashSet;
//
//@Entity
//@Table(name = "roles")
//public class Roles {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @Basic
//    @Column
//    private String role_name;
//
//    @OneToMany(mappedBy = "rolesByRoleId",cascade = CascadeType.ALL,orphanRemoval = true)
//    private Collection<Customers> customers = new HashSet<>();
//}
