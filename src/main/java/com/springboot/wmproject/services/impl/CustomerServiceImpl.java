package com.springboot.wmproject.services.impl;

import com.springboot.wmproject.DTO.CustomerDTO;
import com.springboot.wmproject.entities.Customers;
import com.springboot.wmproject.repositories.CustomerRepository;
import com.springboot.wmproject.services.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService
{
    private CustomerRepository customerRepository;
    private ModelMapper modelMapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CustomerDTO> getAllCustomer() {
        List<Customers> customersList = customerRepository.findAll();
        List<CustomerDTO> customerDTOList = customersList.stream().map(customer -> mapToDto(customer)).collect(Collectors.toList());
        return customerDTOList;
    }

    @Override
    public CustomerDTO getOneCustomerById(Integer id) {
        return null;
    }

    @Override
    public CustomerDTO getOneCustomerByName(String name) {
        return null;
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        return null;
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        return null;
    }

    @Override
    public void deleteCustomer(Integer id) {

    }

    public CustomerDTO mapToDto(Customers customers){
        return modelMapper.map(customers,CustomerDTO.class);
    }

    public Customers mapToEntity(CustomerDTO customerDTO){
        return modelMapper.map(customerDTO,Customers.class);
    }
}
