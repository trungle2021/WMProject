package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders,Integer> {
}
