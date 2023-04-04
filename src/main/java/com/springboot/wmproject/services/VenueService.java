package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.BookingDTO;
import com.springboot.wmproject.DTO.VenueDTO;

import java.util.List;

public interface VenueService {
    List<VenueDTO> getAllVenue();

    List<VenueDTO> getAllVenueActive();
    VenueDTO getOneVenueById(int id);
    List<VenueDTO> getAllVenueByOrderId(Integer orderId);
    List<VenueDTO> getVenueByName(String name);
    VenueDTO createVenue(VenueDTO venueDTO);
    VenueDTO updateVenue(VenueDTO venueDTO);
    void deleteVenue(int venueId);
}
