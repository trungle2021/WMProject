package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.BookingDTO;
import com.springboot.wmproject.entities.Booking;

import java.util.List;

public interface BookingService{
    List<BookingDTO> getAllBooking(Integer customerId);
    BookingDTO getOneBooking(int bookingId);
    BookingDTO createBooking(BookingDTO bookingDTO);
    BookingDTO updateBooking(BookingDTO bookingDTO);
    void deleteBooking(int bookingId);
}
