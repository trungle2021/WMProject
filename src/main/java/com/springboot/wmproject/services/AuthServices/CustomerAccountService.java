package com.springboot.wmproject.services.AuthServices;

import com.springboot.wmproject.DTO.CustomerAccountDTO;
import com.springboot.wmproject.entities.CustomerAccounts;

import java.text.ParseException;
import java.util.List;

public interface CustomerAccountService {
    List<CustomerAccountDTO> findAll();

    CustomerAccountDTO getAccountByAccountId(int id);
    CustomerAccountDTO getAccountByCustomerId(int id);

    CustomerAccountDTO create(CustomerAccountDTO customerAccountDTO);
    CustomerAccountDTO update(CustomerAccountDTO customerAccountDTO);
    void delete(int id);
    CustomerAccountDTO save(CustomerAccountDTO customerAccountDTO);

    CustomerAccountDTO getByResetPasswordToken(String token);
    String updatePassword(String newPass,String token) throws ParseException;
    String updatePasswordMobile(String newPass,String token) throws ParseException;
    String validToken(String token) throws ParseException;
    String processForgotPassword(String email,String userAgent);
    String sendVerifyEmail(String email, String userAgent);
    String verifyEmailRegister(String token) throws ParseException;
    void ExpiredTokenChecker() throws ParseException;

    CustomerAccountDTO findByEmail(String email);

    List<CustomerAccounts> checkUsernameExists(String username);


}
