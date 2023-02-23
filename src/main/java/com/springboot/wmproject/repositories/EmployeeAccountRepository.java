package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.Booking;
import com.springboot.wmproject.entities.EmployeeAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface EmployeeAccountRepository extends JpaRepository<EmployeeAccounts,Integer> {
    @Query("select e from EmployeeAccounts e where e.employeeId=:id")
    List<EmployeeAccounts> getEmployeeAccountByEmployeeId(int id);
}
