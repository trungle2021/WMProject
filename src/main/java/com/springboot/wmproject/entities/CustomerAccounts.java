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
@Table(name = "customer_accounts", schema = "wmproject")
public class CustomerAccounts {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "username", nullable = false, length = 15)
    private String username;
    @Basic
    @Column(name = "password", nullable = false, length = 45)
    private String password;

    @Basic
    @Column(name = "customer_id", nullable = true)
    private Integer customerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id",insertable = false,updatable = false)
    private Customers customersByCustomerId;
    @OneToMany(mappedBy = "customerAccountsByCustomerAccountsId")
    private Collection<PasswordResetToken> passwordResetTokensById;
    @OneToMany(mappedBy = "reviewByCustomerAccountId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<Review> reviewById=new HashSet<>();

}
