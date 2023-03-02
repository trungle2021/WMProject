package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.CustomerAccountDTO;
import com.springboot.wmproject.DTO.EmployeeAccountDTO;
import com.springboot.wmproject.entities.CustomerAccounts;

import java.util.List;
import java.util.Optional;

public interface CustomerAccountService {
    List<CustomerAccountDTO> findAll();

    CustomerAccountDTO getAccountByAccountId(int id);

    CustomerAccountDTO validCustomerAccount(CustomerAccountDTO customerAccountDTO);
    CustomerAccountDTO update(CustomerAccountDTO customerAccountDTO);
    void delete(int id);
//    CustomerAccountDTO findByName(String name);
    CustomerAccountDTO save(CustomerAccountDTO customerAccountDTO);

}
