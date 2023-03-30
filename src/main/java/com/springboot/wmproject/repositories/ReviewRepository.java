package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Integer> {
}
