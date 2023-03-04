package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.VenueDTO;
import com.springboot.wmproject.DTO.VenueImgDTO;
import com.springboot.wmproject.services.VenueImgService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venuesImg")

public class VenueImgController {
    private VenueImgService venueImgService;

    @Autowired
    public VenueImgController(VenueImgService venueImgService) {
        this.venueImgService = venueImgService;
    }

    @GetMapping(value = {"/all"})
    public ResponseEntity<List<VenueImgDTO>> getAll() {
        return ResponseEntity.ok(venueImgService.getAllVenueImg());
    }

    @GetMapping(value = {"/all/venue/{id}"})
    public ResponseEntity<List<VenueImgDTO>> getAllVenueImgById(@PathVariable int id) {
        return ResponseEntity.ok(venueImgService.getAllVenueImgByVenueId(id));
    }

    @PostMapping(value = "/create")
    public ResponseEntity<VenueImgDTO> createVenueImg(@Valid @RequestBody VenueImgDTO venueImgDTO) {
        return new ResponseEntity<>(venueImgService.createVenueImg(venueImgDTO), HttpStatus.CREATED);
    }


    @PutMapping(value = "/update")
    public ResponseEntity<VenueImgDTO> updateVenueImg(@Valid @RequestBody VenueImgDTO venueImgDTO) {
        return ResponseEntity.ok(venueImgService.updateVenueImg(venueImgDTO));
    }

    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<String> deleteVenueImg(@Valid @PathVariable int id) {
        venueImgService.deleteVenueImg(id);
        return ResponseEntity.ok("Venue image has been deleted");
    }
}
