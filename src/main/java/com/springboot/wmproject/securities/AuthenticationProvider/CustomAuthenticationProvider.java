package com.springboot.wmproject.securities.AuthenticationProvider;

import com.springboot.wmproject.exceptions.UserNotFoundException;
import com.springboot.wmproject.securities.AuthenticationToken.CustomerUsernamePasswordAuthenticationToken;
import com.springboot.wmproject.securities.AuthenticationToken.EmployeeUsernamePasswordAuthenticationToken;
import com.springboot.wmproject.securities.UserDetailsService.CustomerDetailsService;
import com.springboot.wmproject.securities.UserDetailsService.EmployeeDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private BCryptPasswordEncoder passwordEncoder;

    private HttpServletRequest request;

    private CustomerDetailsService customerDetailsService;

    private EmployeeDetailsService employeeDetailsService;

    @Autowired
    public CustomAuthenticationProvider(BCryptPasswordEncoder passwordEncoder,HttpServletRequest request,CustomerDetailsService customerDetailsService, EmployeeDetailsService employeeDetailsService) {
        this.customerDetailsService = customerDetailsService;
        this.employeeDetailsService = employeeDetailsService;
        this.request = request;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        //determine which userdetailsservice to use based on the authentication request
        UserDetailsService userDetailsService = determineUserDetailsService(authentication);
        //load user details from the appropriate
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(username.equals(userDetails.getUsername()) && passwordEncoder.matches(password,userDetails.getPassword())){
            return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        }else{
            throw new UserNotFoundException("Invalid username or password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        String path = request.getServletPath();
        if(path.startsWith("/api/auth/employees/login")){
            return authentication.equals(EmployeeUsernamePasswordAuthenticationToken.class);
        }else if(path.startsWith("/api/auth/customers/login")){
            return authentication.equals(CustomerUsernamePasswordAuthenticationToken.class);
        }
        return false;
    }

    private UserDetailsService determineUserDetailsService(Authentication authentication){
       String path = request.getServletPath();
       if(path.startsWith("/api/auth/employees/login")){
           return employeeDetailsService;
       }else if(path.startsWith("/api/auth/customers/login")){
           return customerDetailsService;
       }
        return null;
    }
}
