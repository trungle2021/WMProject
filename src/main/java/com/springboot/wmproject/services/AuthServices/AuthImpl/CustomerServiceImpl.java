package com.springboot.wmproject.services.AuthServices.AuthImpl;

import com.springboot.wmproject.DTO.CustomerDTO;
import com.springboot.wmproject.entities.Customers;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.exceptions.WmAPIException;
import com.springboot.wmproject.repositories.CustomerRepository;
import com.springboot.wmproject.services.AuthServices.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<CustomerDTO> findAll(){
        //find all
        List<Customers> customersList = customerRepository.findAll();
        List<CustomerDTO> customerDTOList = customersList.stream().map(customers -> mapToDto(customers)).collect(Collectors.toList());
        return customerDTOList;
    }

    @Override
    public CustomerDTO getCustomerById(int id) {
        Customers customers=customerRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Customer","id",String.valueOf(id)));
        return mapToDto(customers);
    }
    @Override
    @Transactional
    public CustomerDTO create(CustomerDTO customerDTO) {
        Customers customers = new Customers();
        customers.setFirstName(customerDTO.getFirstname());
        customers.setLastName(customerDTO.getLastname());
        customers.setEmail(customerDTO.getEmail());
        customers.setAddress(customerDTO.getAddress());
        customers.setPhone(customerDTO.getPhone());
        customers.setGender(customerDTO.getGender());
        customers.setAvatar(customerDTO.getAvatar());
        return mapToDto(customerRepository.save(customers));
    }

    @Override
    @Transactional
    public CustomerDTO update(CustomerDTO customerDTO) {
        int customerId = customerDTO.getId();
        if(customerId!=0){
            throw new WmAPIException(HttpStatus.BAD_REQUEST, "CustomerID is required to update");
        }
            //check if customer exist
            Customers checkCustomer=customerRepository.findById(customerId).orElseThrow(()->new ResourceNotFoundException("Customer","id",String.valueOf(customerId)));
                checkCustomer.setFirstName(customerDTO.getFirstname());
                checkCustomer.setLastName(customerDTO.getLastname());
                checkCustomer.setAddress(customerDTO.getAddress());
                checkCustomer.setPhone(customerDTO.getPhone());
                checkCustomer.setEmail(customerDTO.getEmail());
                checkCustomer.setAvatar(customerDTO.getAvatar());
                checkCustomer.setGender(customerDTO.getGender());
                customerRepository.save(checkCustomer);
                return mapToDto(checkCustomer);
    }

    @Override
    @Transactional
    public void delete(int id){
        Customers customers=customerRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Customer","id",String.valueOf(id)));
        customerRepository.delete(customers);
    }

    @Override
    public List<Customers> checkEmailExists(String email) {
        return customerRepository.checkEmailExists(email);
    }

    @Override
    public List<Customers> checkPhoneExists(String phone) {
        return customerRepository.checkPhoneExists(phone);
    }


    public CustomerDTO mapToDto(Customers customers){
        return modelMapper.map(customers,CustomerDTO.class);
    }

    public Customers mapToEntity(CustomerDTO customerDTO){
        return modelMapper.map(customerDTO,Customers.class);
    }
}
