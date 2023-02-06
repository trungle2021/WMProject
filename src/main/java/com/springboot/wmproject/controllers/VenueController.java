package com.springboot.wmproject.controllers;


import com.springboot.wmproject.DTO.BookingDTO;
import com.springboot.wmproject.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venues")
public class VenueController {

    private BookingService bookingService;

    @Autowired
    public VenueController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping(value = {"/customer/{id}","/"})
    public ResponseEntity<List<BookingDTO>> getAll(
            @PathVariable(required = false) Integer id){
        return ResponseEntity.ok(bookingService.getAllBooking(id));
    }

    @PostMapping(value = "/create")
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingDTO bookingDTO){
        return new ResponseEntity<>(bookingService.createBooking(bookingDTO), HttpStatus.CREATED);
    }


    @PutMapping(value = "/update")
    public ResponseEntity<BookingDTO> updateBooking(@RequestBody BookingDTO bookingDTO){
        return ResponseEntity.ok(bookingService.updateBooking(bookingDTO));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable int id){
        bookingService.deleteBooking(id);
        return ResponseEntity.ok("Deleted Booking Successfully");
    }
}
