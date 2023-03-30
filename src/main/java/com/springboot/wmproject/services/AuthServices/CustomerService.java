package com.springboot.wmproject.services.AuthServices;

import com.springboot.wmproject.DTO.CustomerDTO;
import com.springboot.wmproject.entities.Customers;

import java.util.List;

public interface CustomerService{
    List<CustomerDTO> findAll();
    CustomerDTO getCustomerById(int id);
    CustomerDTO getByCustomerAccountId(int id);
    CustomerDTO create(CustomerDTO customerDTO);
    CustomerDTO update(CustomerDTO customerDTO);
    CustomerDTO updateAvatar(CustomerDTO customerDTO);
    void delete(int id);

    List<Customers> checkEmailExists(String email);
    List<Customers> checkPhoneExists(String phone);


}
