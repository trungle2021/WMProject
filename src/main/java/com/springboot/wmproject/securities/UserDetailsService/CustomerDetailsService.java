package com.springboot.wmproject.securities.UserDetailsService;

import com.springboot.wmproject.DTO.CustomerDTO;
import com.springboot.wmproject.entities.CustomerAccounts;
import com.springboot.wmproject.entities.Customers;
import com.springboot.wmproject.exceptions.UserNotFoundException;
import com.springboot.wmproject.exceptions.WmAPIException;
import com.springboot.wmproject.repositories.CustomerAccountRepository;
import com.springboot.wmproject.securities.UserDetails.CustomUserDetails;
import com.springboot.wmproject.services.AuthServices.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomerDetailsService implements UserDetailsService {
    private CustomerAccountRepository customerAccountRepository;
    private CustomerService customerService;
    @Autowired
    public CustomerDetailsService(CustomerAccountRepository customerAccountRepository,CustomerService customerService) {
        this.customerAccountRepository = customerAccountRepository;
        this.customerService = customerService;
    }

    public CustomerDetailsService() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String avatar = "";
       CustomerAccounts customerAccounts = customerAccountRepository.findByUsername(username).orElseThrow(()->new UserNotFoundException("Invalid username or password"));
        CustomerDTO customers = customerService.getCustomerById(customerAccounts.getCustomerId());
        if(!customers.is_verified()){
            throw new WmAPIException(HttpStatus.BAD_REQUEST,"The user has not yet verified their email");
        }
       Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
//        return new User(customerAccounts.getUsername(),customerAccounts.getPassword(), authorities);
        return new CustomUserDetails(customerAccounts.getUsername(),customerAccounts.getPassword(),customerAccounts.getCustomerId().longValue(),customers.is_verified(), authorities);
    }
}


