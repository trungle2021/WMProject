package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.BookingDTO;
import com.springboot.wmproject.DTO.VenueDTO;

import java.util.List;

public interface VenueService {
    List<VenueDTO> getAllVenue();
    List<VenueDTO> getAllVenueByCustomerId(Integer customerId);
    List<VenueDTO> getAllVenueByOrderId(Integer orderId);
    VenueDTO getOneVenue(int venueId);
    VenueDTO createVenue(VenueDTO venueDTO);
    VenueDTO updateVenue(VenueDTO venueDTO);
    void deleteVenue(int venueId);
}
