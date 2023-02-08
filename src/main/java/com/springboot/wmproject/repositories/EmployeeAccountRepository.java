package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.Booking;
import com.springboot.wmproject.entities.EmployeeAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeAccountRepository extends JpaRepository<EmployeeAccounts,Integer> {
}
