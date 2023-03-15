package com.springboot.wmproject.services.AuthServices.AuthImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.wmproject.DTO.*;
import com.springboot.wmproject.exceptions.WmAPIException;
import com.springboot.wmproject.securities.AuthenticationToken.CustomerUsernamePasswordAuthenticationToken;
import com.springboot.wmproject.securities.AuthenticationToken.EmployeeUsernamePasswordAuthenticationToken;
import com.springboot.wmproject.securities.JWT.JwtTokenProvider;
import com.springboot.wmproject.services.AuthServices.*;
import com.springboot.wmproject.services.OrganizeTeamService;
import com.springboot.wmproject.utils.SD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service

public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private EmployeeService employeeService;
    private CustomerService customerService;
    private EmployeeAccountService employeeAccountService;
    private CustomerAccountService customerAccountService;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtTokenProvider tokenProvider;
    private OrganizeTeamService teamService;
    private PasswordResetTokenService passwordResetTokenService;
    private List<String> errors;

    @Autowired
    public AuthServiceImpl(OrganizeTeamService teamService, AuthenticationManager authenticationManager, EmployeeService employeeService, CustomerService customerService, EmployeeAccountService employeeAccountService, CustomerAccountService customerAccountService, BCryptPasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider, PasswordResetTokenService passwordResetTokenService) {
        this.authenticationManager = authenticationManager;
        this.employeeService = employeeService;
        this.customerService = customerService;
        this.employeeAccountService = employeeAccountService;
        this.customerAccountService = customerAccountService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.passwordResetTokenService = passwordResetTokenService;
        this.teamService = teamService;
    }


    @Override
    public String employeeLogin(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager
                .authenticate(new EmployeeUsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);
        return token;
    }

    @Override
    public String customerLogin(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager
                .authenticate(new CustomerUsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateToken(authentication);
        return token;
    }

    @Override
    public RegisterDTO employeeRegister(RegisterDTO registerDTO) throws JsonProcessingException {
        errors = new ArrayList<>();
        OrganizeTeamDTO teamDTO = teamService.getOneOrganizeTeamById(registerDTO.getTeam_id());
        EmployeeDTO employeeDTO = new EmployeeDTO();
        EmployeeAccountDTO employeeAccountDTO = new EmployeeAccountDTO();

        String teamName = teamDTO.getTeamName();
        boolean isPhoneValid = employeeService.checkPhoneExists(registerDTO.getPhone()).size() == 0;
        boolean isEmailValid = employeeService.checkEmailExists(registerDTO.getEmail()).size() == 0;
        boolean isUsernameValid = employeeAccountService.checkUsernameExists(registerDTO.getUsername()).size() == 0;

        if (isPhoneValid) {
            employeeDTO.setPhone(registerDTO.getPhone());
        } else {
            errors.add("Phone number: " + registerDTO.getPhone() + " has already existed");
        }

        if (isEmailValid) {
            employeeDTO.setEmail(registerDTO.getEmail());
        } else {
            errors.add("Email Address : " + registerDTO.getEmail() + " has already existed");
        }

        if (registerDTO.getIsLeader() == 1) {
            List<EmployeeDTO> empInTeam = employeeService.findAllByTeamId(registerDTO.getTeam_id());
            Boolean hasLeaderInTeam = empInTeam.stream().map(emp -> emp.getIsLeader() == 1).findFirst().isPresent();
            if (!hasLeaderInTeam) {
                employeeDTO.setIsLeader(1);
            } else {
                errors.add(teamDTO.getTeamName() + " already has a leader");
            }
        }

        if (isUsernameValid) {
            employeeAccountDTO.setUsername(registerDTO.getUsername());
        } else {
            errors.add("Username : " + registerDTO.getUsername() + " has already existed");
        }

        if (errors.size() != 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            String responseError = objectMapper.writeValueAsString(errors);
            throw new WmAPIException(HttpStatus.BAD_REQUEST, responseError);
        }
        employeeDTO.setName(registerDTO.getName());
        employeeDTO.setSalary(registerDTO.getSalary());
        employeeDTO.setAddress(registerDTO.getAddress());
        employeeDTO.setJoinDate(registerDTO.getJoinDate());
        employeeDTO.setIsLeader(registerDTO.getIsLeader());

        employeeDTO.setTeam_id(registerDTO.getTeam_id());
        employeeDTO.setGender(registerDTO.getGender());
        employeeDTO.setAvatar(registerDTO.getAvatar());
        EmployeeDTO empDTO = employeeService.create(employeeDTO);
        //getID after employee created -> then pass to employee account
        employeeAccountDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        String role = "";

        if (teamName.equals(SD.TEAM_ADMINISTRATOR)) {
            role = SD.ROLE_SALE;
        } else {
            role = SD.ROLE_ORGANIZE;
        }
        employeeAccountDTO.setRole(role);
        employeeAccountDTO.setEmployeeId(empDTO.getId());
        employeeAccountService.create(employeeAccountDTO);
        registerDTO.setEmployeeId(empDTO.getId());
        registerDTO.setRole(role);

        return registerDTO;
    }

    @Override
    public RegisterCustomerDTO customerRegister(RegisterCustomerDTO registerDTO) throws JsonProcessingException {
        errors = new ArrayList<>();
        boolean isPhoneValid = customerService.checkPhoneExists(registerDTO.getPhone()).size() == 0;
        boolean isEmailValid = customerService.checkEmailExists(registerDTO.getEmail()).size() == 0;
        boolean isUsernameValid = customerAccountService.checkUsernameExists(registerDTO.getUsername()).size() == 0;

        CustomerDTO customerDTO = new CustomerDTO();
        CustomerAccountDTO customerAccountDTO = new CustomerAccountDTO();

        //valid
        if (isPhoneValid) {
            customerDTO.setPhone(registerDTO.getPhone());
        } else {
            errors.add("Phone number: " + registerDTO.getPhone() + " has already existed");
        }
        if (isEmailValid) {
            customerDTO.setEmail(registerDTO.getEmail());
        } else {
            errors.add("Email Address : " + registerDTO.getEmail() + " has already existed");
        }

        if (isUsernameValid) {
            customerAccountDTO.setUsername(registerDTO.getUsername());
        } else {
            errors.add("Username : " + registerDTO.getUsername() + " has already existed");
        }

        if (errors.size() != 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            String responseError = objectMapper.writeValueAsString(errors);
            throw new WmAPIException(HttpStatus.BAD_REQUEST, responseError);
        }

        customerDTO.setFirstname(registerDTO.getFirstname());
        customerDTO.setLastname(registerDTO.getLastname());
        customerDTO.setAddress(registerDTO.getAddress());
        customerDTO.setGender(registerDTO.getGender());
        customerDTO.setAvatar(registerDTO.getAvatar());
        CustomerDTO cusDTO = customerService.create(customerDTO);
        //getID after employee created -> then pass to employee account

        customerAccountDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        customerAccountDTO.setCustomerId(cusDTO.getId());
        customerAccountService.create(customerAccountDTO);
        registerDTO.setCustomerId(cusDTO.getId());
        return registerDTO;
    }

    @Override
    public RegisterDTO employeeUpdate(RegisterDTO registerDTO) throws JsonProcessingException {
        //check emp exist by id
       EmployeeDTO empExist = employeeService.getEmployeeById(registerDTO.getEmployeeId());
        EmployeeAccountDTO empHasAccount = employeeAccountService.getEmployeeAccountByEmployeeId(registerDTO.getEmployeeId());
        errors = new ArrayList<>();
        OrganizeTeamDTO teamDTO = teamService.getOneOrganizeTeamById(registerDTO.getTeam_id());
        EmployeeDTO employeeDTO = new EmployeeDTO();
        EmployeeAccountDTO employeeAccountDTO = new EmployeeAccountDTO();
        String teamName = teamDTO.getTeamName();
        boolean isPhoneValid = employeeService.checkPhoneExists(registerDTO.getPhone()).size() == 0;
        boolean isEmailValid = employeeService.checkEmailExists(registerDTO.getEmail()).size() == 0;
        boolean isUsernameValid = employeeAccountService.checkUsernameExists(registerDTO.getUsername()).size() == 0;

        if(!empExist.getPhone().equals(registerDTO.getPhone())){
            if (isPhoneValid) {
                employeeDTO.setPhone(registerDTO.getPhone());
            } else {
                errors.add("Phone number: " + registerDTO.getPhone() + " has already existed");
            }
        }else{
            employeeDTO.setPhone(registerDTO.getPhone());

        }


        if(!empExist.getEmail().equalsIgnoreCase(registerDTO.getEmail().trim())){
            if (isEmailValid) {
                employeeDTO.setEmail(registerDTO.getEmail());
            } else {
                errors.add("Email Address : " + registerDTO.getEmail() + " has already existed");
            }
        }else{
            employeeDTO.setEmail(registerDTO.getEmail());
        }

       if(empExist.getIsLeader() != registerDTO.getIsLeader()){
           if (registerDTO.getIsLeader() == 1 ) {
               List<EmployeeDTO> empInTeam = employeeService.findAllByTeamId(registerDTO.getTeam_id());
               Boolean hasLeaderInTeam = empInTeam.stream().map(emp -> emp.getIsLeader() == 1).findFirst().isPresent();
               if (!hasLeaderInTeam) {
                   employeeDTO.setIsLeader(1);
               } else {
                   errors.add(teamDTO.getTeamName() + " already has a leader");
               }
           }
       }else{
           employeeDTO.setIsLeader(registerDTO.getIsLeader());
       }

        if(!empHasAccount.getUsername().equalsIgnoreCase(registerDTO.getUsername())){
            if (isUsernameValid) {
                employeeAccountDTO.setUsername(registerDTO.getUsername());
            } else {
                errors.add("Username : " + registerDTO.getUsername() + " has already existed");
            }
        }else{
            employeeAccountDTO.setUsername(registerDTO.getUsername());
        }


        if (errors.size() != 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            String responseError = objectMapper.writeValueAsString(errors);
            throw new WmAPIException(HttpStatus.BAD_REQUEST, responseError);
        }
        employeeDTO.setId(registerDTO.getEmployeeId());
        employeeDTO.setName(registerDTO.getName());
        employeeDTO.setSalary(registerDTO.getSalary());
        employeeDTO.setAddress(registerDTO.getAddress());
        employeeDTO.setJoinDate(registerDTO.getJoinDate());
        employeeDTO.setIsLeader(registerDTO.getIsLeader());
        employeeDTO.setTeam_id(registerDTO.getTeam_id());
        employeeDTO.setGender(registerDTO.getGender());
        employeeDTO.setAvatar(registerDTO.getAvatar());
        employeeDTO.setId(empExist.getId());
        EmployeeDTO empDTO = employeeService.update(employeeDTO);
        //getID after employee created -> then pass to employee account


        if(!registerDTO.getPassword().equals("") || !registerDTO.getPassword().isEmpty() || !registerDTO.getPassword().isBlank()){
            employeeAccountDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        }else{
            employeeAccountDTO.setPassword(empHasAccount.getPassword());
        }
        String role = "";

        if (teamName.equals(SD.TEAM_ADMINISTRATOR)) {
            role = SD.ROLE_SALE;
        } else {
            role = SD.ROLE_ORGANIZE;
        }

        employeeAccountDTO.setId(empHasAccount.getId());
        employeeAccountDTO.setRole(role);
        employeeAccountDTO.setEmployeeId(empDTO.getId());
        employeeAccountService.update(employeeAccountDTO);
        registerDTO.setEmployeeId(empDTO.getId());
        registerDTO.setRole(role);

        return registerDTO;
    }

    @Override
    public RegisterCustomerDTO customerUpdate(RegisterCustomerDTO registerDTO) throws JsonProcessingException {
        //check cus exist by id
        customerService.getCustomerById(registerDTO.getId());
        errors = new ArrayList<>();
        boolean isPhoneValid = customerService.checkPhoneExists(registerDTO.getPhone()).size() == 0;
        boolean isEmailValid = customerService.checkEmailExists(registerDTO.getEmail()).size() == 0;
        boolean isUsernameValid = customerAccountService.checkUsernameExists(registerDTO.getUsername()).size() == 0;

        CustomerDTO customerDTO = new CustomerDTO();
        CustomerAccountDTO customerAccountDTO = new CustomerAccountDTO();


        if (isPhoneValid) {
            customerDTO.setPhone(registerDTO.getPhone());
        } else {
            errors.add("Phone number: " + registerDTO.getPhone() + " has already existed");
        }
        if (isEmailValid) {
            customerDTO.setEmail(registerDTO.getEmail());
        } else {
            errors.add("Email Address : " + registerDTO.getEmail() + " has already existed");
        }

        if (isUsernameValid) {
            customerAccountDTO.setUsername(registerDTO.getUsername());
        } else {
            errors.add("Username : " + registerDTO.getUsername() + " has already existed");
        }


        if (errors.size() != 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            String responseError = objectMapper.writeValueAsString(errors);
            throw new WmAPIException(HttpStatus.BAD_REQUEST, responseError);
        }

        customerDTO.setFirstname(registerDTO.getFirstname());
        customerDTO.setLastname(registerDTO.getLastname());
        customerDTO.setAddress(registerDTO.getAddress());
        customerDTO.setGender(registerDTO.getGender());
        customerDTO.setAvatar(registerDTO.getAvatar());
        CustomerDTO cusDTO = customerService.update(customerDTO);
        //getID after employee created -> then pass to employee account

        if(!registerDTO.getPassword().equals("") || !registerDTO.getPassword().isEmpty() || !registerDTO.getPassword().isBlank()){
            customerAccountDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        }
        customerAccountDTO.setCustomerId(cusDTO.getId());
        customerAccountService.update(customerAccountDTO);
        registerDTO.setCustomerId(cusDTO.getId());
        return registerDTO;
    }

    @Override
    public RegisterDTO getOneRegisterEmp(int empID) throws JsonProcessingException {
        EmployeeDTO employeeDTO = employeeService.getEmployeeById(empID);
        EmployeeAccountDTO employeeAccountDTO = employeeAccountService.getEmployeeAccountByEmployeeId(empID);
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setName(employeeDTO.getName());
        registerDTO.setAddress(employeeDTO.getAddress());
        registerDTO.setPhone(employeeDTO.getPhone());
        registerDTO.setJoinDate(employeeDTO.getJoinDate());
        registerDTO.setSalary(employeeDTO.getSalary());
        registerDTO.setEmail(employeeDTO.getEmail());
        registerDTO.setIsLeader(employeeDTO.getIsLeader());
        registerDTO.setTeam_id(employeeDTO.getOrganizeTeamsByTeamId().getId());
        registerDTO.setGender(employeeDTO.getGender());
        registerDTO.setAvatar(employeeDTO.getAvatar());
        registerDTO.setUsername(employeeAccountDTO.getUsername());
        registerDTO.setRole(employeeAccountDTO.getRole());
        registerDTO.setEmployeeId(empID);
        return registerDTO;
    }

    @Override
    public RegisterCustomerDTO getOneRegisterCustomer(int customerID) throws JsonProcessingException {
        CustomerDTO customerDTO = customerService.getCustomerById(customerID);
        CustomerAccountDTO customerAccountDTO = customerAccountService.getAccountByCustomerId(customerID);
        RegisterCustomerDTO registerDTO = new RegisterCustomerDTO();
        registerDTO.setFirstname(customerDTO.getFirstname());
        registerDTO.setLastname(customerDTO.getLastname());
        registerDTO.setAddress(customerDTO.getAddress());
        registerDTO.setPhone(customerDTO.getPhone());
        registerDTO.setEmail(customerDTO.getEmail());
        registerDTO.setGender(customerDTO.getGender());
        registerDTO.setAvatar(customerDTO.getAvatar());
        registerDTO.setUsername(customerAccountDTO.getUsername());
        registerDTO.setCustomerId(customerID);
        return registerDTO;
    }
}
