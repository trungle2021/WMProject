package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.Venues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VenueRepository extends JpaRepository<Venues,Integer> {
    @Query("select a from Venues a where a.ordersById = :orderId")
    List<Venues> getAllByOrderId(Integer orderId);
    @Query("select a from Venues a where a.venueName like %:name% ")
    List<Venues> getAllVenuesByName(String name);
    @Query("select a from Venues a where a.venueName =:name ")
    List<Venues> validVenueByName(String name);
}
