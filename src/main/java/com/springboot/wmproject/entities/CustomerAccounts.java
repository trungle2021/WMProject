package com.springboot.wmproject.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer_account", schema = "wmproject", catalog = "")
public class CustomerAccounts {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "phone", nullable = false, length = 15)
    private String phone;
    @Basic
    @Column(name = "pin", nullable = false, length = 45)
    private String pin;
    @Basic
    @Column(name = "customer_id", nullable = true)
    private Integer customerId;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id",nullable = false,insertable = false,updatable = false)
    private Customers customers;
}
