package com.springboot.wmproject.services.AuthServices;

import com.springboot.wmproject.DTO.CustomerAccountDTO;
import com.springboot.wmproject.entities.CustomerAccounts;
import com.springboot.wmproject.entities.PasswordResetToken;

public interface PasswordResetTokenService {
    void create(CustomerAccountDTO accountDTO, String token);
    void delete(String token);
}
