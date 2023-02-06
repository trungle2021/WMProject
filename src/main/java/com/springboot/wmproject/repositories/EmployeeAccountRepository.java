package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.EmployeeAccounts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeAccountRepository extends JpaRepository<EmployeeAccounts,Integer> {
}
