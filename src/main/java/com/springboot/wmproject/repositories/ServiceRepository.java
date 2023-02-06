package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.Services;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Services,Integer> {
}
