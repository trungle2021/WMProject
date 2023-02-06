package com.springboot.wmproject.controllers;


import com.springboot.wmproject.entities.Booking;
import com.springboot.wmproject.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking){
        return new ResponseEntity<>(bookingService.createBooking(booking), HttpStatus.CREATED);
    }

    @GetMapping(value = "/customer/{id}")
    public ResponseEntity<List<Booking>> getAll(@PathVariable int id){
        return ResponseEntity.ok(bookingService.getAllBooking(id));
    }
}
