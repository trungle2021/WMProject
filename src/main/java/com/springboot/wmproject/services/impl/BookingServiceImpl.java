package com.springboot.wmproject.services.impl;

import com.springboot.wmproject.entities.Booking;
import com.springboot.wmproject.entities.Customers;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
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
    public List<Booking> getAllBooking(Integer customerId) {
        if(customerId == null){
            return bookingRepository.findAll();
        }
        return bookingRepository.findAllById(customerId);
    }

    @Override
    public Booking getOneBooking(int bookingId) {
        return null;
    }

    @Override
    public Booking createBooking(Booking booking) {
        int customerId = booking.getCustomerId();
        //check if customerId is empty, default is 0
        if( customerId != 0){
            Customers customer = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer","id",String.valueOf(customerId)));
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
        int _customerId = booking.getCustomerId();
        if( _customerId != 0){
            Customers customer = customerRepository.findById(_customerId).orElseThrow(() -> new ResourceNotFoundException("Customer","id",String.valueOf(_customerId)));
            //check valid customerID
            //if valid, create new booking
            if(customer != null){
                Booking updateBooking = bookingRepository.findById(_customerId).orElseThrow(() -> new ResourceNotFoundException("Booking","id",String.valueOf(booking.getId())));
                if(updateBooking!=null){
                    updateBooking.setBookingDate(booking.getBookingDate());
                    updateBooking.setCustomerId(booking.getCustomerId());
                    updateBooking.setAppointmentDate(booking.getAppointmentDate());
                    return bookingRepository.save(updateBooking);
                }
            }
        }
        return null;
    }

    @Override
    public void deleteBooking(int bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking","id",String.valueOf(bookingId)));
        bookingRepository.delete(booking);
    }
}