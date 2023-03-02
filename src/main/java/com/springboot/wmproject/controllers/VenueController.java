package com.springboot.wmproject.controllers;


import com.springboot.wmproject.DTO.BookingDTO;
import com.springboot.wmproject.DTO.VenueDTO;
import com.springboot.wmproject.services.BookingService;
import com.springboot.wmproject.services.OrderService;
import com.springboot.wmproject.services.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venues")
public class VenueController {

    private VenueService venueService;
    private OrderService orderService;

    @Autowired
    public VenueController(VenueService venueService, OrderService orderService) {
        this.venueService = venueService;
        this.orderService = orderService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = {"/all"})
    public ResponseEntity<List<VenueDTO>> getAll() {
        return ResponseEntity.ok(venueService.getAllVenue());
    }
    @GetMapping(value = {"/all/orders/{id}"})
    public ResponseEntity<List<VenueDTO>> getAllByOrderId(@Valid @PathVariable int id) {
        return ResponseEntity.ok(venueService.getAllVenueByOrderId(id));
    }
    @GetMapping(value = {"/one/{id}"})
    public ResponseEntity<VenueDTO> getOneById(@Valid @PathVariable int id) {
        return ResponseEntity.ok(venueService.getOneVenueById(id));
    }
    @GetMapping(value = {"/all/{name}"})
    public ResponseEntity<List<VenueDTO>> getAllByName(@Valid @PathVariable String name) {
        return ResponseEntity.ok(venueService.getVenueByName(name));
    }

    @PostMapping(value = "/create")
    public ResponseEntity<VenueDTO> createVenue(@Valid @RequestBody VenueDTO venueDTO) {
        return new ResponseEntity<>(venueService.createVenue(venueDTO), HttpStatus.CREATED);
    }


    @PutMapping(value = "/update")
    public ResponseEntity<VenueDTO> updateVenue(@Valid @RequestBody VenueDTO venueDTO) {
        return ResponseEntity.ok(venueService.updateVenue(venueDTO));
    }
    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<String> deleteVenue(@Valid @PathVariable int id){
        venueService.deleteVenue(id);
        return ResponseEntity.ok("Venue has been deleted");
    }

}
