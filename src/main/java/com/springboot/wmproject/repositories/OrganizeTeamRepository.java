package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizeTeamRepository extends JpaRepository<Booking,Integer> {
}
