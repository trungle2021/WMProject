package com.springboot.wmproject.security;

import com.springboot.wmproject.entities.EmployeeAccounts;
import com.springboot.wmproject.entities.Employees;
import com.springboot.wmproject.repositories.CustomerRepository;
import com.springboot.wmproject.repositories.EmployeeAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomEmployeeDetailService implements UserDetailsService {

    EmployeeAccountRepository employeeAccountRepository;

    @Autowired
    public CustomEmployeeDetailService(EmployeeAccountRepository employeeAccountRepository) {
        this.employeeAccountRepository = employeeAccountRepository;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//       EmployeeAccounts user = employeeAccountRepository.findByU
        return null;
    }
}
