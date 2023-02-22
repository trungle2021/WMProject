package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.CustomerDTO;

import java.util.List;

public interface CustomerService{
    List<CustomerDTO> getAllCustomer();
    CustomerDTO getOneCustomerById(Integer id);
    CustomerDTO getOneCustomerByName(String name);

    CustomerDTO createCustomer(CustomerDTO customerDTO);

    CustomerDTO updateCustomer(CustomerDTO customerDTO);
    void deleteCustomer(Integer id);

}
