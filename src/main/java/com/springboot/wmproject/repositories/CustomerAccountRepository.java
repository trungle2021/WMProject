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
    @Query("select ca FROM CustomerAccounts ca JOIN Customers c ON ca.customerId = c.id where c.email =:email")
    CustomerAccounts findByEmail(String email);

    @Query("select ca.id, ca.username, ca.password, ca.passwordResetToken, ca.customerId FROM CustomerAccounts ca JOIN PasswordResetToken c ON ca.customerId = c.id where c.token =:token")
    CustomerAccounts getByResetPasswordToken(String token);
}
