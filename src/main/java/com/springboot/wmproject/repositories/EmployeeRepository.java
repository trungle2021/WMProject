package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employees,Integer> {
    @Query("select e from Employees e join EmployeeAccounts ea on e.id = ea.employeeId where ea.role like %:role% ")
    List<Employees> findAllByRole(String role);
    @Query("select e from Employees e where e.name like %:name%")
    List<Employees> findAllByName(String name);
    @Query("select e from Employees e where e.team_id = :teamId")
    List<Employees> findAllTeamId(Integer teamId);

    @Query("select e from Employees e where e.phone = :phone")
    List<Employees> checkPhoneExists(String phone);

    @Query("select e from Employees e where e.email = :email")
    List<Employees> checkEmailExists(String email);


}
