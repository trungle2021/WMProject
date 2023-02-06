package com.springboot.wmproject.services;

import com.springboot.wmproject.entities.Booking;

import java.util.List;

public interface BookingService{
    List<Booking> getAllBooking(int customerId);
    Booking getOneBooking(int bookingId);
    Booking createBooking(Booking booking);
    Booking updateBooking(Booking booking);
    void deleteBooking(int bookingId);
}
