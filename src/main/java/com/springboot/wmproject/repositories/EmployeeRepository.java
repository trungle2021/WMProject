package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employees,Integer> {
    @Query("select e from Employees e where e.empType like %:empType% ")
    List<Employees> findAllByEmpType(String empType);
    @Query("select e from Employees e where e.name like %:name%")
    List<Employees> findAllByName(String name);

}
