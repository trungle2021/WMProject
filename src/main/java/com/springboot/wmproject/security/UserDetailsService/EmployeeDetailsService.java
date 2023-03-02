package com.springboot.wmproject.security.UserDetailsService;

import com.springboot.wmproject.entities.EmployeeAccounts;
import com.springboot.wmproject.repositories.EmployeeAccountRepository;
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
               .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(employeeAccounts.getRole()));
        return new User(employeeAccounts.getUsername(),employeeAccounts.getPassword(), authorities);
    }
}
