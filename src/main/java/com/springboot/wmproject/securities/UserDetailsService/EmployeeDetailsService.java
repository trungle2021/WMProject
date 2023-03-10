package com.springboot.wmproject.securities.UserDetailsService;

import com.springboot.wmproject.entities.EmployeeAccounts;
import com.springboot.wmproject.exceptions.UserNotFoundException;
import com.springboot.wmproject.repositories.EmployeeAccountRepository;
import com.springboot.wmproject.securities.UserDetails.CustomUserDetails;
//import com.springboot.wmproject.securities.UserDetails.CustomerUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class EmployeeDetailsService implements UserDetailsService {

    EmployeeAccountRepository employeeAccountRepository;

    @Autowired
    public EmployeeDetailsService(EmployeeAccountRepository employeeAccountRepository) {
        this.employeeAccountRepository = employeeAccountRepository;
    }

    public EmployeeDetailsService() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      EmployeeAccounts employeeAccounts =  employeeAccountRepository.findByUsername(username)
               .orElseThrow(()->new UserNotFoundException("Invalid username or password"));

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(employeeAccounts.getRole()));
        return new CustomUserDetails(employeeAccounts.getUsername(),employeeAccounts.getPassword(),employeeAccounts.getEmployeeId().longValue() ,authorities);

    }
}
