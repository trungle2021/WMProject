package com.springboot.wmproject.services.AuthServices;

import com.springboot.wmproject.DTO.CustomerAccountDTO;
import com.springboot.wmproject.entities.CustomerAccounts;
import com.springboot.wmproject.entities.PasswordResetToken;

import java.text.ParseException;
import java.util.Calendar;

public interface PasswordResetTokenService {
    String create(int customerAccountID);
    void delete(String token);
    String validatePasswordResetToken(String token) throws ParseException;
    boolean isTokenFound(PasswordResetToken passToken);
    boolean isTokenExpired(PasswordResetToken passToken) throws ParseException;


}
