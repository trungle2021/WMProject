package com.springboot.wmproject.services.impl;

import com.springboot.wmproject.DTO.EmployeeAccountDTO;
import com.springboot.wmproject.DTO.EmployeeDTO;
import com.springboot.wmproject.DTO.LoginDTO;
import com.springboot.wmproject.DTO.RegisterDTO;
import com.springboot.wmproject.entities.EmployeeAccounts;
import com.springboot.wmproject.entities.Employees;
import com.springboot.wmproject.security.JwtTokenProvider;
import com.springboot.wmproject.services.AuthService;
import com.springboot.wmproject.services.EmployeeAccountService;
import com.springboot.wmproject.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private EmployeeService employeeService;
    private EmployeeAccountService employeeAccountService;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtTokenProvider tokenProvider;

    @Autowired
    public AuthServiceImpl(JwtTokenProvider tokenProvider,AuthenticationManager authenticationManager, EmployeeService employeeService, EmployeeAccountService employeeAccountService,BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.employeeService = employeeService;
        this.employeeAccountService = employeeAccountService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public String login(LoginDTO loginDTO) {
       Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

       String token =  tokenProvider.generateToken(authentication);
       return token;
    }

    @Override
    @Transactional
    public String register(RegisterDTO registerDTO) {

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName(registerDTO.getName());
        employeeDTO.setPhone(registerDTO.getPhone());
        employeeDTO.setSalary(registerDTO.getSalary());
        employeeDTO.setAddress(registerDTO.getAddress());
        employeeDTO.setJoinDate(registerDTO.getJoinDate());
        employeeDTO.setEmpType(registerDTO.getEmpType());
        employeeDTO.setTeam_id(registerDTO.getTeam_id());
        EmployeeDTO empDTO = employeeService.validEmployee(employeeDTO);
        EmployeeDTO empResponse = employeeService.save(empDTO);
        //getID after employee created -> then pass to employee account

        EmployeeAccountDTO employeeAccountDTO = new EmployeeAccountDTO();

        employeeAccountDTO.setUsername(registerDTO.getUsername());
        employeeAccountDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        employeeAccountDTO.setRole(registerDTO.getRole());
        employeeAccountDTO.setEmployeeId(empResponse.getId());
        EmployeeAccountDTO empAccDTO = employeeAccountService.validEmployeeAccount(employeeAccountDTO);


        employeeAccountService.save(empAccDTO);
        return "Register New Employee Successfully";
    }


}
