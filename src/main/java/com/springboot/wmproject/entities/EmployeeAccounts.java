package com.springboot.wmproject.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "employee_accounts", schema = "wmproject", catalog = "")
public class EmployeeAccounts {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "username", nullable = true, length = 45)
    private String username;
    @Basic
    @Column(name = "password", nullable = true, length = 45)
    private String password;
    @Basic
    @Column(name = "role", nullable = true, length = 45)
    private String role;
    @Basic
    @Column(name = "employee_id", nullable = true)
    private Integer employeeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id",insertable = false,updatable = false)
    private Employees employeesByEmployeeId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeAccounts that = (EmployeeAccounts) o;
        return id == that.id && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(role, that.role) && Objects.equals(employeeId, that.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, role, employeeId);
    }

    public Employees getEmployeesByEmployeeId() {
        return employeesByEmployeeId;
    }

    public void setEmployeesByEmployeeId(Employees employeesByEmployeeId) {
        this.employeesByEmployeeId = employeesByEmployeeId;
    }
}
