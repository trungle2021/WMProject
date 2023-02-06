package com.springboot.wmproject.services.impl;

import com.springboot.wmproject.entities.Booking;
import com.springboot.wmproject.entities.Customers;
import com.springboot.wmproject.repositories.BookingRepository;
import com.springboot.wmproject.repositories.CustomerRepository;
import com.springboot.wmproject.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepository;
    private CustomerRepository customerRepository;
    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, CustomerRepository customerRepository) {
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
    }


    @Override
    public List<Booking> getAllBooking(int customerId) {

        return bookingRepository.findAllById(customerId);
    }

    @Override
    public Booking getOneBooking(int bookingId) {
        return null;
    }

    @Override
    public Booking createBooking(Booking booking) {
        int customerId = booking.getCustomers().getId();
        //check if customerId is empty, default is 0
        if( customerId != 0){
            Customers customer = customerRepository.findById(customerId).orElseThrow();
            //check valid customerID
            //if valid, create new booking
            if(customer != null){
                Booking newBooking = new Booking();
                newBooking.setCustomers(customer);
                newBooking.setCustomerId(customerId);
                //get current date time
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date date = new Date();
                newBooking.setBookingDate(formatter.format(date));
                newBooking.setAppointmentDate(booking.getAppointmentDate());
                return bookingRepository.save(newBooking);
            }
        }
        return null;
    }

    @Override
    public Booking updateBooking(Booking booking) {
        return null;
    }

    @Override
    public void deleteBooking(int bookingId) {
//        Booking booking = bookingRepository.findById(bookingId);
    }
}