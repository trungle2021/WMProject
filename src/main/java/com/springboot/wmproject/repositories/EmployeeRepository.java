package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.Employees;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employees,Integer> {
}
