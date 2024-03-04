package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    @Query("select r from RefreshToken r where r.token =:refreshToken")
    Optional<RefreshToken> getOneByRefreshToken(String refreshToken);

    @Query("select r from RefreshToken r Join CustomerAccounts c ON r.customerId = c.id where r.customerId =:customerId")
    Optional<RefreshToken> getOneByCustomerId(int customerId);

    @Query("select r from RefreshToken r Join EmployeeAccounts e ON r.employeeId = e.id where r.employeeId =:employeeId")
    Optional<RefreshToken> getOneByEmployeeId(int employeeId);

    Optional<RefreshToken> findByToken(String token);
}
