package com.springboot.wmproject.repositories;

import com.springboot.wmproject.DTO.CustomerDTO;
import com.springboot.wmproject.entities.Customers;
import com.springboot.wmproject.entities.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customers,Integer> {
    @Query("select c from Customers c where c.phone = :phone")
    List<Customers> checkPhoneExists(String phone);

    @Query("select c from Customers c where c.email = :email")
    List<Customers> checkEmailExists(String email);

    @Query("select c from Customers c JOIN CustomerAccounts  ca ON c.id = ca.customerId where ca.id = :id")

    Customers getByCustomerAccountId(int id);
}


