package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodDetailRepository extends JpaRepository<Booking,Integer> {
}
