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
public class Employees {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "name", nullable = true, length = 45)
    private String name;
    @Basic
    @Column(name = "address", nullable = true, length = 100)
    private String address;
    @Basic
    @Column(name = "phone", nullable = true, length = 45)
    private String phone;
    @Basic
    @Column(name = "join_date", nullable = true, length = 45)
    private String joinDate;
    @Basic
    @Column(name = "salary", nullable = true, precision = 2)
    private Double salary;
    @Basic
    @Column(name = "emp_type", nullable = true, length = 20)
    private String empType;
    @Basic
    @Column(name = "team_id", nullable = true)
    private Integer teamId;
    @JsonManagedReference
    @OneToMany(mappedBy = "employees",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<EmployeeAccounts> employeeAccounts;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", referencedColumnName = "id",nullable = false,insertable = false,updatable = false)
    private OrganizeTeams organizeTeams;
    @JsonManagedReference
    @OneToMany(mappedBy = "employees",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<Orders> orders;


}
