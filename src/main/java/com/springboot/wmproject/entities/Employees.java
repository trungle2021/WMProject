package com.springboot.wmproject.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@Getter
@Setter
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

    @Basic
    @Column(name = "avatar", nullable = true)
    private String avatar;

    @OneToOne(mappedBy = "employeesByEmployeeId",cascade = CascadeType.ALL,orphanRemoval = true)
    private EmployeeAccounts employeeAccountsById;
    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id",insertable = false,updatable = false)
    private OrganizeTeams organizeTeamsByTeamId;
    @OneToMany(mappedBy = "employeesByBookingEmp",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<Orders> ordersById = new HashSet<>();


}
