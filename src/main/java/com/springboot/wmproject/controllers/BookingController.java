package com.springboot.wmproject.controllers;


import com.springboot.wmproject.DTO.BookingDTO;
import com.springboot.wmproject.entities.Booking;
import com.springboot.wmproject.services.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAnyRole('ADMIN','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = {"/all","/all/customer/{customerId}"})
    public ResponseEntity<List<BookingDTO>> GetAllBooking(
            @PathVariable(required = false) Integer customerId){
        return ResponseEntity.ok(bookingService.getAllBooking(customerId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = {"{booking_id}","{booking_id}/customer/{customer_id}"})
    public ResponseEntity<BookingDTO> GetOneBooking(
            @PathVariable(required = false) Integer booking_id, @PathVariable(required = false) Integer customer_id){
        return ResponseEntity.ok(bookingService.getOneBooking(booking_id, customer_id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/create")
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingDTO bookingDTO){
        return new ResponseEntity<>(bookingService.createBooking(bookingDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = "/update")
    public ResponseEntity<BookingDTO> updateBooking(@RequestBody BookingDTO bookingDTO){
        return ResponseEntity.ok(bookingService.updateBooking(bookingDTO));
    }
    @PreAuthorize("hasAnyRole('ADMIN','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable int id){
        bookingService.deleteBooking(id);
        return ResponseEntity.ok("Deleted Booking Successfully");
    }
}
