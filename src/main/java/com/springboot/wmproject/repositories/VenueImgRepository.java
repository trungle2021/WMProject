package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.VenueImages;
import com.springboot.wmproject.entities.Venues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VenueImgRepository extends JpaRepository<VenueImages,Integer> {
    @Query("select a from VenueImages a where a.venueId=:id")
    List<VenueImages> getAllById(int id);
}
