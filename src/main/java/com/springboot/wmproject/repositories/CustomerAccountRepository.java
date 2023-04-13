package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.CustomerAccounts;
import com.springboot.wmproject.entities.EmployeeAccounts;
import com.springboot.wmproject.entities.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerAccountRepository extends JpaRepository<CustomerAccounts,Integer> {

    @Query("select c from CustomerAccounts c JOIN Customers cu ON c.customerId = cu.id where c.customerId=:customerId")
    CustomerAccounts getCustomerAccountByCustomerId(int customerId);
    Optional<CustomerAccounts> findByUsername(String username);
    @Query("select ca FROM CustomerAccounts ca JOIN Customers c ON ca.customerId = c.id where c.email =:email")
    CustomerAccounts findByEmail(String email);

    @Query("select ca FROM CustomerAccounts ca JOIN PasswordResetToken p ON ca.id = p.customerAccountsId where p.token =:token")
    CustomerAccounts getByResetPasswordToken(String token);

    @Query("select c from CustomerAccounts c where c.username = :username")
    List<CustomerAccounts> checkUsernameExists(String username);


}
