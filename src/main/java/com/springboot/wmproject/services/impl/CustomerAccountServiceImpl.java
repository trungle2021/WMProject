package com.springboot.wmproject.services.impl;

import com.springboot.wmproject.DTO.CustomerAccountDTO;
import com.springboot.wmproject.DTO.CustomerDTO;
import com.springboot.wmproject.entities.CustomerAccounts;
import com.springboot.wmproject.entities.Customers;
import com.springboot.wmproject.entities.EmployeeAccounts;
import com.springboot.wmproject.entities.Employees;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.exceptions.WmAPIException;
import com.springboot.wmproject.repositories.CustomerAccountRepository;
import com.springboot.wmproject.repositories.CustomerRepository;
import com.springboot.wmproject.services.CustomerAccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerAccountServiceImpl implements CustomerAccountService {
    private CustomerAccountRepository customerAccountRepository;
    private CustomerRepository customerRepository;
    private ModelMapper modelMapper;

    @Autowired
    public CustomerAccountServiceImpl(CustomerAccountRepository customerAccountRepository, CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerAccountRepository = customerAccountRepository;
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CustomerAccountDTO> findAll() {
        List<CustomerAccounts> accountsList = customerAccountRepository.findAll();

        return accountsList.stream().map(account -> mapToDto(account)).collect(Collectors.toList());
    }

    @Override
    public CustomerAccountDTO getAccountByAccountId(int id) {
        return null;
    }


    @Override
    public CustomerAccountDTO validCustomerAccount(CustomerAccountDTO customerAccountDTO) {

        int customerID = customerAccountDTO.getCustomerId();

        if (customerID != 0) {
            //check if employee exist
            Customers customers = customerRepository.findById(customerID).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", String.valueOf(customerID)));
            //if employee info exist -> able to create account
            if (customers != null) {
                //check if username exist
                Optional<CustomerAccounts> customerAccounts= customerAccountRepository.findByUsername(customerAccountDTO.getUsername());
                if(customerAccounts.isPresent()){
                    throw new WmAPIException(HttpStatus.BAD_REQUEST,"Username already existed");
                }
                return customerAccountDTO;

            }

        }
        return null;
    }

    @Override
    public CustomerAccountDTO update(CustomerAccountDTO customerAccountDTO) {
        int customerID = customerAccountDTO.getCustomerId();
        //check employee account exist
        CustomerAccounts customerAccounts = customerAccountRepository.findById(customerID).orElseThrow(() -> new ResourceNotFoundException("Customer Account", "id", String.valueOf(customerID)));
        //if exist update
        if (customerAccounts != null) {
            CustomerAccounts updateCustomer = new CustomerAccounts();
            updateCustomer.setId(customerAccountDTO.getId());
            updateCustomer.setUsername(customerAccountDTO.getUsername());
            updateCustomer.setPassword(customerAccountDTO.getPassword());
            updateCustomer.setCustomerId(customerAccountDTO.getCustomerId());
            return mapToDto(customerAccountRepository.save(updateCustomer));
        }
        return null;
    }

    @Override
    public void delete(int id) {
        CustomerAccounts customerAccounts = customerAccountRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("CustomerAccount","id",String.valueOf(id)));
       if(customerAccounts != null){
           customerAccountRepository.delete(customerAccounts);
       }
    }

    @Override
    public CustomerAccountDTO save(CustomerAccountDTO employeeAccountDTO) {
        return null;
    }

    public CustomerAccountDTO mapToDto(CustomerAccounts customerAccounts){
        return modelMapper.map(customerAccounts,CustomerAccountDTO.class);
    }

    public CustomerAccounts mapToEntity(CustomerAccountDTO customerAccountDTO){
        return modelMapper.map(customerAccountDTO,CustomerAccounts.class);
    }


}