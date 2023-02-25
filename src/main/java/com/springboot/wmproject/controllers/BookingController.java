package com.springboot.wmproject.controllers;


import com.springboot.wmproject.DTO.BookingDTO;
import com.springboot.wmproject.entities.Booking;
import com.springboot.wmproject.services.BookingService;
import io.swagger.v3.oas.annotations.Operation;
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

    @GetMapping(value = {"/all","/all/customer/{customerId}"})
    public ResponseEntity<List<BookingDTO>> GetAllBooking(
            @PathVariable(required = false) Integer customerId){
        return ResponseEntity.ok(bookingService.getAllBooking(customerId));
    }


    @GetMapping(value = {"{booking_id}","{booking_id}/customer/{customer_id}"})
    public ResponseEntity<BookingDTO> GetOneBooking(
            @PathVariable(required = false) Integer booking_id, @PathVariable(required = false) Integer customer_id){
        return ResponseEntity.ok(bookingService.getOneBooking(booking_id, customer_id));
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
