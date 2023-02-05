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
@Table(name = "employees")
public class Employee {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", length = 45)
    private String name;

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "phone", length = 45)
    private String phone;

    @Column(name = "join_date", length = 45)
    private String joinDate;

    @Column(name = "salary", precision = 15, scale = 2)
    private Double salary;

    @Column(name = "emp_type", length = 20)
    private String empType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId")
    private OrganizeTeam team;


}