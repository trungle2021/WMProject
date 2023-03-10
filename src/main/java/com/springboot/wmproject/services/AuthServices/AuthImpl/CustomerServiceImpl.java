package com.springboot.wmproject.services.AuthServices.AuthImpl;

import com.springboot.wmproject.DTO.CustomerDTO;
import com.springboot.wmproject.entities.Customers;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.repositories.CustomerRepository;
import com.springboot.wmproject.services.AuthServices.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public CustomerDTO create(CustomerDTO customerDTO) {
        return mapToDto(customerRepository.save(mapToEntity(customerDTO)));
    }

    @Override
    public CustomerDTO update(CustomerDTO customerDTO) {
        int customerId = customerDTO.getId();
        if(customerId!=0){
            //check if customer exist
            Customers checkCustomer=customerRepository.findById(customerId).orElseThrow(()->new ResourceNotFoundException("Customer","id",String.valueOf(customerId)));
            //if customer = null create new
            if(checkCustomer!=null){
                Customers customers=new Customers();
                customers.setId(customerDTO.getId());
                customers.setName(customerDTO.getName());
                customers.setAddress(customerDTO.getAddress());
                customers.setPhone(customerDTO.getPhone());
                customers.setAvatar(customerDTO.getAvatar());
                customers.setGender(customerDTO.getGender());
                customerRepository.save(customers);
                return mapToDto(customers);
            }
        }
        return null;
    }

    @Override
    public void delete(int id){
        Customers customers=customerRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Customer","id",String.valueOf(id)));
        customerRepository.delete(customers);
    }





    public CustomerDTO mapToDto(Customers customers){
        return modelMapper.map(customers,CustomerDTO.class);
    }

    public Customers mapToEntity(CustomerDTO customerDTO){
        return modelMapper.map(customerDTO,Customers.class);
    }
}
