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

    @GetMapping(value = {"/customer/{id}","/"})
    public ResponseEntity<List<Booking>> getAll(
            @PathVariable(required = false) Integer id){
        return ResponseEntity.ok(bookingService.getAllBooking(id));
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking){
        return new ResponseEntity<>(bookingService.createBooking(booking), HttpStatus.CREATED);
    }


    @PutMapping(value = "/update")
    public ResponseEntity<Booking> updateBooking(@RequestBody Booking booking){
        return ResponseEntity.ok(bookingService.updateBooking(booking));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable int id){
        bookingService.deleteBooking(id);
        return ResponseEntity.ok("Deleted Booking Successfully");
    }
}
