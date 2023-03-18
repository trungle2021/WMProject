package com.springboot.wmproject.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "password_reset_token", schema = "wmproject")
public class PasswordResetToken {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private long id;
    @Basic
    @Column(name = "expiry_date", nullable = true)
    private String expiryDate;
    @Basic
    @Column(name = "token", nullable = true, length = 255)
    private String token;
    @Basic
    @Column(name = "customer_accounts_id", nullable = false)
    private int customerAccountsId;
    @ManyToOne
    @JoinColumn(name = "customer_accounts_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    private CustomerAccounts customerAccountsByCustomerAccountsId;
//    @ManyToOne
//    @JoinColumn(name = "employee_accounts_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
//    private CustomerAccounts  employeeAccountByEmployeeAccountsId;

}
