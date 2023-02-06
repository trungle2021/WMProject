package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.Materials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Materials,Integer> {
}
