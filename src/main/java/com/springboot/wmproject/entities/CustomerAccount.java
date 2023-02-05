package com.springboot.wmproject.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_account")
public class CustomerAccount {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "pin", nullable = false, length = 45)
    private String pin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId")
    private Customer customer;


}