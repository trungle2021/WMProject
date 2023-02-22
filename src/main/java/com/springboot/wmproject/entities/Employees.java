package com.springboot.wmproject.entities;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

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

    @OneToMany(mappedBy = "employeesByEmployeeId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<EmployeeAccounts> employeeAccountsById = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id",insertable = false,updatable = false)
    private OrganizeTeams organizeTeamsByTeamId;
    @OneToMany(mappedBy = "employeesByBookingEmp",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<Orders> ordersById = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getEmpType() {
        return empType;
    }

    public void setEmpType(String empType) {
        this.empType = empType;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employees employees = (Employees) o;
        return id == employees.id && Objects.equals(name, employees.name) && Objects.equals(address, employees.address) && Objects.equals(phone, employees.phone) && Objects.equals(joinDate, employees.joinDate) && Objects.equals(salary, employees.salary) && Objects.equals(empType, employees.empType) && Objects.equals(teamId, employees.teamId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, phone, joinDate, salary, empType, teamId);
    }

    public Collection<EmployeeAccounts> getEmployeeAccountsById() {
        return employeeAccountsById;
    }

    public void setEmployeeAccountsById(Collection<EmployeeAccounts> employeeAccountsById) {
        this.employeeAccountsById = employeeAccountsById;
    }

    public OrganizeTeams getOrganizeTeamsByTeamId() {
        return organizeTeamsByTeamId;
    }

    public void setOrganizeTeamsByTeamId(OrganizeTeams organizeTeamsByTeamId) {
        this.organizeTeamsByTeamId = organizeTeamsByTeamId;
    }

    public Collection<Orders> getOrdersById() {
        return ordersById;
    }

    public void setOrdersById(Collection<Orders> ordersById) {
        this.ordersById = ordersById;
    }
}
