package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.Services;
import com.springboot.wmproject.entities.Venues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Services,Integer> {
    @Query("select c from Services c where c.serviceName =:name ")
    List<Services> validServiceByName(String name);
}
