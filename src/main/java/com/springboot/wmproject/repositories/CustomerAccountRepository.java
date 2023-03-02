package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.CustomerAccounts;
import com.springboot.wmproject.entities.EmployeeAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerAccountRepository extends JpaRepository<CustomerAccounts,Integer> {
    Optional<CustomerAccounts> findByUsername(String username);

}
