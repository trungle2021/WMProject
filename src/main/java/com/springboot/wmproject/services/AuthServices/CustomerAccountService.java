package com.springboot.wmproject.services.AuthServices;

import com.springboot.wmproject.DTO.CustomerAccountDTO;
import com.springboot.wmproject.entities.CustomerAccounts;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface CustomerAccountService {
    List<CustomerAccountDTO> findAll();

    CustomerAccountDTO getAccountByAccountId(int id);

    CustomerAccountDTO create(CustomerAccountDTO customerAccountDTO);
    CustomerAccountDTO update(CustomerAccountDTO customerAccountDTO);
    void delete(int id);
    CustomerAccountDTO save(CustomerAccountDTO customerAccountDTO);

    CustomerAccountDTO getByResetPasswordToken(String token);
    String updatePassword(String newPass,String token) throws ParseException;
    String processForgotPassword(String email);
    CustomerAccountDTO findByEmail(String email);

    List<CustomerAccounts> checkUsernameExists(String username);


}
