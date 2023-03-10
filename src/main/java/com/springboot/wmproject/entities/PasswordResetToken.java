package com.springboot.wmproject.entities;

import com.springboot.wmproject.DTO.CustomerAccountDTO;
import jakarta.persistence.Entity;
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
public class PasswordResetToken {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(targetEntity = CustomerAccounts.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "customer_accounts_id")
    private CustomerAccounts customerAccount;
    private Date expiryDate = new Date(EXPIRATION);

    public PasswordResetToken(CustomerAccounts customerAccount,String token) {
        this.token = token;
        this.customerAccount = customerAccount;
    }
}
