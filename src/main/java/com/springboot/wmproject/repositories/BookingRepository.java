package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface BookingRepository extends JpaRepository<Booking,Integer> {
    @Query("SELECT a from Booking a where a.customerId = :customerId")
    List<Booking> findAllById(int customerId);
}
