package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.QueryType;

import java.util.List;


public interface BookingRepository extends JpaRepository<Booking,Integer> {
    @Query("SELECT a from Booking a where a.customerId = :customerId")
//    @QueryType
    List<Booking> findAllById(int customerId);

    @Query("SELECT a from Booking a where a.id = :bookingId and a.customerId = :customerId")
//    @QueryType(QueryType.READ)
    Booking findByBookingIdAndCustomerId(Integer bookingId,Integer customerId);

}
