package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.ServiceDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceDetailRepository extends JpaRepository<ServiceDetails,Integer> {
}
