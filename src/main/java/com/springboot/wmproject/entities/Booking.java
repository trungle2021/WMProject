package com.springboot.wmproject.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Booking {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "customer_id", nullable = true)
    private Integer customerId;
    @Basic
    @Column(name = "booking_date", nullable = true, length = 45)
    private String bookingDate;
    @Basic
    @Column(name = "appointment_date", nullable = true, length = 45)
    private String appointmentDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id",insertable = false,updatable = false)
    private Customers customersByCustomerId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return id == booking.id && Objects.equals(customerId, booking.customerId) && Objects.equals(bookingDate, booking.bookingDate) && Objects.equals(appointmentDate, booking.appointmentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, bookingDate, appointmentDate);
    }

    public Customers getCustomersByCustomerId() {
        return customersByCustomerId;
    }

    public void setCustomersByCustomerId(Customers customersByCustomerId) {
        this.customersByCustomerId = customersByCustomerId;
    }
}
