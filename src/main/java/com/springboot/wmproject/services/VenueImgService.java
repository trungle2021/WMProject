package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.VenueImgDTO;

import java.util.List;

public interface VenueImgService {
    List<VenueImgDTO> getAllVenueImg();
    List<VenueImgDTO> getAllVenueImgByVenueId(int id);
    VenueImgDTO createVenueImg(VenueImgDTO venueImgDTO);
    VenueImgDTO updateVenueImg(VenueImgDTO venueImgDTO);
    void deleteVenueImg(int id);
}
