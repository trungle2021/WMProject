package com.springboot.wmproject.repositories;

import com.springboot.wmproject.DTO.EmployeeAccountDTO;
import com.springboot.wmproject.entities.Booking;
import com.springboot.wmproject.entities.CustomerAccounts;
import com.springboot.wmproject.entities.EmployeeAccounts;
import com.springboot.wmproject.entities.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeAccountRepository extends JpaRepository<EmployeeAccounts,Integer> {
    @Query("select ea from EmployeeAccounts ea JOIN Employees e ON ea.employeeId = e.id where ea.employeeId=:employeeId")
    EmployeeAccounts getEmployeeAccountByEmployeeId(int employeeId);
    Optional<EmployeeAccounts> findByUsername(String username);


    @Query("select e from EmployeeAccounts e LEFT JOIN Employees f  where f.name LIKE %:name%")
    List<EmployeeAccounts> findByName(String name);

    @Query("select e from EmployeeAccounts e LEFT JOIN Employees f  where e.role=:role")
    List<EmployeeAccounts> filterByRole(String role);

    @Query("select e from EmployeeAccounts e where e.username = :username")
    List<EmployeeAccounts> checkUsernameExists(String username);


}
