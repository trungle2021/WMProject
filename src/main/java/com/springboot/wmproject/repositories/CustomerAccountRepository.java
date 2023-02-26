package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.CustomerAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerAccountRepository extends JpaRepository<CustomerAccounts,Integer> {

}
