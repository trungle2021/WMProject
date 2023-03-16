package com.springboot.wmproject.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

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
    @Column(name = "email", nullable = true)
    private String email;

    @Basic
    @Column(name = "gender", nullable = true)
    private String gender;

    @Basic
    @Column(nullable = false, columnDefinition = "TINYINT(1)", length = 1)
    private boolean isLeader;

    @Basic
    @Column(name = "team_id", nullable = true)
    private Integer team_id;

    @Basic
    @Column(name = "avatar", nullable = true)
    private String avatar;

    @OneToMany(mappedBy = "employeesByEmployeeId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<EmployeeAccounts> employeeAccountsById=new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id",insertable = false,updatable = false)
    private OrganizeTeams organizeTeamsByTeamId;
    @OneToMany(mappedBy = "employeesByBookingEmp",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<Orders> ordersById = new HashSet<>();

}
