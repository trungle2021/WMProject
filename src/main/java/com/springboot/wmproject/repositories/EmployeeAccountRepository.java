package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.Booking;
import com.springboot.wmproject.entities.CustomerAccounts;
import com.springboot.wmproject.entities.EmployeeAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeAccountRepository extends JpaRepository<EmployeeAccounts,Integer> {
    @Query("select e from EmployeeAccounts e JOIN Employees f ON e.id = f.id where e.employeeId=:employeeId and f.id=:employeeId")
    EmployeeAccounts getEmployeeAccountByEmployeeId(int employeeId);
    Optional<EmployeeAccounts> findByUsername(String username);

    @Query("select e from EmployeeAccounts e LEFT JOIN Employees f  ON e.id = f.id  where f.name LIKE %:name%")
    List<EmployeeAccounts> findByName(String name);

    @Query("select e from EmployeeAccounts e LEFT JOIN Employees f ON e.id = f.id where f.empType=:empType")
    List<EmployeeAccounts> filterByEmpType(String empType);

//    @Query("DELETE FROM EmployeeAccounts e where e.id=:id")
//    void delete(int id);
}
