package com.springboot.wmproject.services.AuthServices;

import com.springboot.wmproject.DTO.CustomerAccountDTO;
import com.springboot.wmproject.entities.CustomerAccounts;

import java.util.List;
import java.util.Optional;

public interface CustomerAccountService {
    List<CustomerAccountDTO> findAll();

    CustomerAccountDTO getAccountByAccountId(int id);

    CustomerAccountDTO create(CustomerAccountDTO customerAccountDTO);
    CustomerAccountDTO update(CustomerAccountDTO customerAccountDTO);
    void delete(int id);
    CustomerAccountDTO save(CustomerAccountDTO customerAccountDTO);
    CustomerAccountDTO findByEmail(String email);

    CustomerAccountDTO getByResetPasswordToken(String token);
    void updatePassword(CustomerAccountDTO customerAccountDTO, String newPass,String token);
}
