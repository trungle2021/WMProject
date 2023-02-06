package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.Venues;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venues,Integer> {
}
