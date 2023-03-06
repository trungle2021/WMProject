package com.springboot.wmproject.security.UserDetailsService;

import com.springboot.wmproject.entities.CustomerAccounts;
import com.springboot.wmproject.exceptions.UserNotFoundException;
import com.springboot.wmproject.repositories.CustomerAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomerDetailsService implements UserDetailsService {
    private CustomerAccountRepository customerAccountRepository;

    @Autowired
    public CustomerDetailsService(CustomerAccountRepository customerAccountRepository) {
        this.customerAccountRepository = customerAccountRepository;
    }

    public CustomerDetailsService() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       CustomerAccounts customerAccounts = customerAccountRepository.findByUsername(username).orElseThrow(()->new UserNotFoundException("Employee","username",username));
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        return new User(customerAccounts.getUsername(),customerAccounts.getPassword(), authorities);
    }
}


