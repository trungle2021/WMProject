package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.CustomerDTO;
import com.springboot.wmproject.DTO.EmployeeDTO;

import java.util.List;

public interface CustomerService{
    List<CustomerDTO> findAll();
    CustomerDTO getCustomerById(int id);
    CustomerDTO create(CustomerDTO customerDTO);
    CustomerDTO update(CustomerDTO customerDTO);
    void delete(int id);


}
