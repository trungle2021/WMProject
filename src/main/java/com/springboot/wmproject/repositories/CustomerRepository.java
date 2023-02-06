package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.Customers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customers,Integer> {
}
