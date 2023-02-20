package com.springboot.wmproject.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "customer_accounts", schema = "wmproject", catalog = "")
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id",insertable = false,updatable = false)
    private Customers customersByCustomerId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerAccounts that = (CustomerAccounts) o;
        return id == that.id && Objects.equals(phone, that.phone) && Objects.equals(pin, that.pin) && Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phone, pin, customerId);
    }

    public Customers getCustomersByCustomerId() {
        return customersByCustomerId;
    }

    public void setCustomersByCustomerId(Customers customersByCustomerId) {
        this.customersByCustomerId = customersByCustomerId;
    }
}
