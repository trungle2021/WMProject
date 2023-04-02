package com.springboot.wmproject.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "refresh_token", schema = "wmproject", catalog = "")
public class RefreshToken {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Basic
    @Column(name = "customer_id")
    private Integer customerId;
    @Basic
    @Column(name = "employee_id")
    private Integer employeeId;
    @Basic
    @Column(name = "expiry_date")
    private String expiryDate;
    @Basic
    @Column(name = "token")
    private String token;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id",insertable = false,updatable = false)
    private CustomerAccounts customerAccountsByCustomerId;
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id",insertable = false,updatable = false)
    private EmployeeAccounts employeeAccountsByEmployeeId;

}
