package com.springboot.wmproject.services.AuthServices.AuthImpl;

import com.springboot.wmproject.DTO.*;
//import com.springboot.wmproject.security.AuthenticationToken.CustomerUsernamePasswordAuthenticationToken;
import com.springboot.wmproject.securities.AuthenticationToken.CustomerUsernamePasswordAuthenticationToken;
import com.springboot.wmproject.securities.AuthenticationToken.EmployeeUsernamePasswordAuthenticationToken;
import com.springboot.wmproject.securities.JWT.JwtTokenProvider;
import com.springboot.wmproject.services.AuthServices.*;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private EmployeeService employeeService;
    private CustomerService customerService;
    private EmployeeAccountService employeeAccountService;
    private CustomerAccountService customerAccountService;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtTokenProvider tokenProvider;
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, EmployeeService employeeService, CustomerService customerService, EmployeeAccountService employeeAccountService, CustomerAccountService customerAccountService, BCryptPasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider,PasswordResetTokenService passwordResetTokenService) {
        this.authenticationManager = authenticationManager;
        this.employeeService = employeeService;
        this.customerService = customerService;
        this.employeeAccountService = employeeAccountService;
        this.customerAccountService = customerAccountService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.passwordResetTokenService = passwordResetTokenService;
    }

    @Override
    public String employeeLogin(LoginDTO loginDTO) {
        Authentication authentication =  authenticationManager
                .authenticate(new EmployeeUsernamePasswordAuthenticationToken(loginDTO.getUsername(),loginDTO.getPassword()));

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authentication);
        String token =  tokenProvider.generateToken(authentication);
        return token;
    }

    @Override
    public String customerLogin(LoginDTO loginDTO) {
        Authentication authentication =  authenticationManager
                .authenticate(new CustomerUsernamePasswordAuthenticationToken(loginDTO.getUsername(),loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token =  tokenProvider.generateToken(authentication);
        return token;
    }

    @Override
    @Transactional
    public RegisterDTO employeeRegister(RegisterDTO registerDTO) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName(registerDTO.getName());
        employeeDTO.setPhone(registerDTO.getPhone());
        employeeDTO.setSalary(registerDTO.getSalary());
        employeeDTO.setAddress(registerDTO.getAddress());
        employeeDTO.setJoinDate(registerDTO.getJoinDate());
        employeeDTO.setEmpType(registerDTO.getEmpType());
        employeeDTO.setTeam_id(registerDTO.getTeam_id());
        employeeDTO.setAvatar(registerDTO.getAvatar());
        EmployeeDTO empDTO = employeeService.create(employeeDTO);
        //getID after employee created -> then pass to employee account

        EmployeeAccountDTO employeeAccountDTO = new EmployeeAccountDTO();
        employeeAccountDTO.setUsername(registerDTO.getUsername());
        employeeAccountDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        employeeAccountDTO.setRole(registerDTO.getRole());
        employeeAccountDTO.setEmployeeId(empDTO.getId());
        employeeAccountService.create(employeeAccountDTO);
        registerDTO.setEmployeeId(empDTO.getId());
        return registerDTO;
    }

    @Override
    public RegisterDTO customerRegister(RegisterDTO registerDTO) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName(registerDTO.getName());
        customerDTO.setPhone(registerDTO.getPhone());
        customerDTO.setAddress(registerDTO.getAddress());
        customerDTO.setGender(registerDTO.getGender());
        customerDTO.setAvatar(registerDTO.getAvatar());
        CustomerDTO cusDTO = customerService.create(customerDTO);
        //getID after employee created -> then pass to employee account

        CustomerAccountDTO customerAccountDTO = new CustomerAccountDTO();
        customerAccountDTO.setUsername(registerDTO.getUsername());
        customerAccountDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        customerAccountDTO.setCustomerId(cusDTO.getId());
        customerAccountService.create(customerAccountDTO);
        registerDTO.setEmployeeId(cusDTO.getId());
        return null;
    }

    @Override
    public GenericResponse resetPassword(String email) {
       CustomerAccountDTO accountDTO =  customerAccountService.findByEmail(email);
       String token = RandomString.make(30);
       passwordResetTokenService.create(accountDTO,token);
        return null;
    }
}
