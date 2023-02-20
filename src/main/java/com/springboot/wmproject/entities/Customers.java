package com.springboot.wmproject.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@Entity
public class Customers {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @Basic
    @Column(name = "address", nullable = false, length = 100)
    private String address;
    @Basic
    @Column(name = "phone", nullable = false, length = 15)
    private String phone;
    @Basic
    @Column(name = "gender", nullable = true, length = 10)
    private String gender;

    @Basic
    @Column(name = "avatar", nullable = true)
    private String avatar;
    @OneToMany(mappedBy = "customersByCustomerId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<Booking> bookingsById = new HashSet<>();
    @OneToMany(mappedBy = "customersByCustomerId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<CustomerAccounts> customerAccountsById = new HashSet<>();
    @OneToMany(mappedBy = "customersByCustomerId",cascade = CascadeType.ALL,orphanRemoval = true)
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customers customers = (Customers) o;
        return id == customers.id && Objects.equals(name, customers.name) && Objects.equals(address, customers.address) && Objects.equals(phone, customers.phone) && Objects.equals(gender, customers.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, phone, gender);
    }

    public Collection<Booking> getBookingsById() {
        return bookingsById;
    }

    public void setBookingsById(Collection<Booking> bookingsById) {
        this.bookingsById = bookingsById;
    }

    public Collection<CustomerAccounts> getCustomerAccountsById() {
        return customerAccountsById;
    }

    public void setCustomerAccountsById(Collection<CustomerAccounts> customerAccountsById) {
        this.customerAccountsById = customerAccountsById;
    }

    public Collection<Orders> getOrdersById() {
        return ordersById;
    }

    public void setOrdersById(Collection<Orders> ordersById) {
        this.ordersById = ordersById;
    }
}
