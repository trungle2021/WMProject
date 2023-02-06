package com.springboot.wmproject.services.impl;

import com.springboot.wmproject.DTO.BookingDTO;
import com.springboot.wmproject.entities.Booking;
import com.springboot.wmproject.entities.Customers;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.repositories.BookingRepository;
import com.springboot.wmproject.repositories.CustomerRepository;
import com.springboot.wmproject.services.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepository;
    private CustomerRepository customerRepository;
    private ModelMapper modelMapper;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<BookingDTO> getAllBooking(Integer customerId) {
        if(customerId == null){
            List<Booking> bookingList = bookingRepository.findAll();
            List<BookingDTO> bookingDTOList =  bookingList.stream().map(booking -> mapToDto(booking)).collect(Collectors.toList());
            return bookingDTOList;
        }
        return bookingRepository.findAllById(customerId).stream().map(booking ->mapToDto(booking)).collect(Collectors.toList());
    }

    @Override
    public BookingDTO getOneBooking(int bookingId) {
        return null;
    }

    @Override
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        //get customerID from DTO
        int customerId = bookingDTO.getCustomerId();
        //check if customerId is empty, default is 0
        if( customerId != 0){
            Customers customer = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer","id",String.valueOf(customerId)));
            //check valid customerID
            //if valid, create new booking
            if(customer != null){
                //convert DTO to entity
                Booking booking = mapToEntity(bookingDTO);
                //get current date time
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date date = new Date();
                booking.setBookingDate(formatter.format(date));
                //save booking
                Booking newBooking = bookingRepository.save(booking);

                BookingDTO bookingResponse = mapToDto(newBooking);
                return bookingResponse;
            }
        }
        return null;
    }

    @Override
    public BookingDTO updateBooking(BookingDTO bookingDTO) {
        int _customerId = bookingDTO.getCustomerId();
        if( _customerId != 0){
            Customers customer = customerRepository.findById(_customerId).orElseThrow(() -> new ResourceNotFoundException("Customer","id",String.valueOf(_customerId)));
            //check valid customerID
            //if valid, create new booking
            if(customer != null){
                Booking booking = bookingRepository.findById(bookingDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Booking","id",String.valueOf(bookingDTO.getId())));
                if(booking!=null){
                    booking.setBookingDate(bookingDTO.getBookingDate());
                    booking.setCustomerId(bookingDTO.getCustomerId());
                    booking.setAppointmentDate(bookingDTO.getAppointmentDate());
                    Booking updatedBooking = bookingRepository.save(booking);
                    return mapToDto(updatedBooking);
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

    public BookingDTO mapToDto(Booking booking){
        BookingDTO postDto = modelMapper.map(booking, BookingDTO.class);
        return postDto;
    }

    public Booking mapToEntity(BookingDTO bookingDTO){
        Booking booking = modelMapper.map(bookingDTO, Booking.class);
        return booking;
    }
}