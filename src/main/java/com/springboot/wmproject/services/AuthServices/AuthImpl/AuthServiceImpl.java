package com.springboot.wmproject.services.AuthServices.AuthImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.wmproject.DTO.*;
import com.springboot.wmproject.entities.Employees;
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
    @Transactional
    public String staffDelete(int id) {
        EmployeeAccountDTO employeeAccountDTO =  employeeAccountService.getEmployeeAccountByEmployeeId(id);
        employeeService.delete(id);
        employeeAccountService.delete(employeeAccountDTO.getId());
        return "Employee Deleted Successfully ";
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
        boolean isValidPhone = employeeService.checkPhoneExists(registerDTO.getPhone()).size() == 0;
        boolean isValidEmail = employeeService.checkEmailExists(registerDTO.getEmail()).size() == 0;
        boolean isValidUsername = employeeAccountService.checkUsernameExists(registerDTO.getUsername()).size() == 0;
        boolean isValidPassword = registerDTO.getPassword() != null && !registerDTO.getPassword().isEmpty() && !registerDTO.getPassword().trim().isBlank();

        if (isValidPhone) {
            employeeDTO.setPhone(registerDTO.getPhone());
        } else {
            errors.add("Phone number: " + registerDTO.getPhone() + " has already existed");
        }

        if (isValidEmail) {
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

        if (isValidUsername) {
            employeeAccountDTO.setUsername(registerDTO.getUsername());
        } else {
            errors.add("Username : " + registerDTO.getUsername() + " has already existed");
        }

        if(isValidPassword){
            employeeAccountDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        } else {
            errors.add("Password cannot be empty");
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
        employeeDTO.setIs_deleted(0);
        EmployeeDTO empDTO = employeeService.create(employeeDTO);
        //getID after employee created -> then pass to employee account

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
        boolean isValidPassword = registerDTO.getPassword() != null && !registerDTO.getPassword().isEmpty() && !registerDTO.getPassword().trim().isBlank();

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

        if(isValidPassword){
            customerAccountDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        } else {
            errors.add("Password cannot be empty");
        }

        if (errors.size() != 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            String responseError = objectMapper.writeValueAsString(errors);
            throw new WmAPIException(HttpStatus.BAD_REQUEST, responseError);
        }

        customerDTO.setFirst_name(registerDTO.getFirst_name());
        customerDTO.setLast_name(registerDTO.getLast_name());
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
        errors = new ArrayList<>();
        EmployeeDTO employeeDTO = new EmployeeDTO();
        EmployeeAccountDTO employeeAccountDTO = new EmployeeAccountDTO();
        OrganizeTeamDTO teamDTO = teamService.getOneOrganizeTeamById(registerDTO.getTeam_id());
        String teamName = teamDTO.getTeamName();
        //check emp exist by id
        EmployeeDTO empExist = employeeService.getEmployeeById(registerDTO.getEmployeeId());
        EmployeeAccountDTO empHasAccount = employeeAccountService.getEmployeeAccountByEmployeeId(registerDTO.getEmployeeId());

        //Validate
        boolean isValidPhone = employeeService.checkPhoneExists(registerDTO.getPhone()).size() == 0;
        boolean isValidEmail = employeeService.checkEmailExists(registerDTO.getEmail()).size() == 0;
        boolean isValidUsername = employeeAccountService.checkUsernameExists(registerDTO.getUsername()).size() == 0;
        boolean isValidPassword = registerDTO.getPassword() != null && !registerDTO.getPassword().isEmpty() && !registerDTO.getPassword().trim().isBlank();
        boolean phoneHasChanged = !empExist.getPhone().equals(registerDTO.getPhone());
        boolean emailHasChanged = !empExist.getEmail().equalsIgnoreCase(registerDTO.getEmail().trim());
        boolean userNameHasChanged = !empHasAccount.getUsername().equalsIgnoreCase(registerDTO.getUsername());
        boolean leaderHasChanged = empExist.getIsLeader() != registerDTO.getIsLeader();


        if(phoneHasChanged){
            if (isValidPhone) {
                employeeDTO.setPhone(registerDTO.getPhone());
            } else {
                errors.add("Phone number: " + registerDTO.getPhone() + " has already existed");
            }
        }else{
            employeeDTO.setPhone(registerDTO.getPhone());
        }

        if(emailHasChanged){
            if (isValidEmail) {
                employeeDTO.setEmail(registerDTO.getEmail());
            } else {
                errors.add("Email Address : " + registerDTO.getEmail() + " has already existed");
            }
        }else{
            employeeDTO.setEmail(registerDTO.getEmail());
        }

       if(leaderHasChanged){
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

        if(userNameHasChanged){
            if (isValidUsername) {
                employeeAccountDTO.setUsername(registerDTO.getUsername());
            } else {
                errors.add("Username : " + registerDTO.getUsername() + " has already existed");
            }
        }else{
            employeeAccountDTO.setUsername(empHasAccount.getUsername());
        }

        if(isValidPassword){
            employeeAccountDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        }else{
            employeeAccountDTO.setPassword(empHasAccount.getPassword());
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
        errors = new ArrayList<>();
        CustomerDTO customerDTO = new CustomerDTO();
        CustomerAccountDTO customerAccountDTO = new CustomerAccountDTO();
        //check cus exist by id
        CustomerDTO cusExist = customerService.getCustomerById(registerDTO.getId());
        CustomerAccountDTO cusHasAccount = customerAccountService.getAccountByCustomerId(registerDTO.getCustomerId());

        //Validate
        boolean isValidPhone = customerService.checkPhoneExists(registerDTO.getPhone()).size() == 0;
        boolean isValidEmail = customerService.checkEmailExists(registerDTO.getEmail()).size() == 0;
        boolean isValidUsername = customerAccountService.checkUsernameExists(registerDTO.getUsername()).size() == 0;
        boolean isValidPassword = registerDTO.getPassword() != null && !registerDTO.getPassword().isEmpty() && !registerDTO.getPassword().trim().isBlank();
        boolean phoneHasChanged = !cusExist.getPhone().equals(registerDTO.getPhone());
        boolean emailHasChanged = !cusExist.getEmail().equalsIgnoreCase(registerDTO.getEmail().trim());
        boolean userNameHasChanged = !cusHasAccount.getUsername().equalsIgnoreCase(registerDTO.getPhone());


        if(phoneHasChanged){
            if (isValidPhone) {
                customerDTO.setPhone(registerDTO.getPhone());
            } else {
                errors.add("Phone number: " + registerDTO.getPhone() + " has already existed");
            }
        }else{
            customerDTO.setPhone(registerDTO.getPhone());
        }

        if(emailHasChanged){
            if (isValidEmail) {
                customerDTO.setEmail(registerDTO.getEmail());
            } else {
                errors.add("Email Address : " + registerDTO.getEmail() + " has already existed");
            }
        }else{
            customerDTO.setEmail(registerDTO.getEmail());
        }

        if(userNameHasChanged){
            if (isValidUsername) {
                customerAccountDTO.setUsername(registerDTO.getUsername());
            } else {
                errors.add("Username : " + registerDTO.getUsername() + " has already existed");
            }
        }else{
            customerAccountDTO.setUsername(cusHasAccount.getUsername());
        }

        if(isValidPassword){
            customerAccountDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        }else{
            customerAccountDTO.setPassword(cusHasAccount.getPassword());
        }

        if (errors.size() != 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            String responseError = objectMapper.writeValueAsString(errors);
            throw new WmAPIException(HttpStatus.BAD_REQUEST, responseError);
        }

        customerDTO.setFirst_name(registerDTO.getFirst_name());
        customerDTO.setLast_name(registerDTO.getLast_name());
        customerDTO.setAddress(registerDTO.getAddress());
        customerDTO.setGender(registerDTO.getGender());
        customerDTO.setAvatar(registerDTO.getAvatar());
        customerDTO.setId(cusExist.getId());
        CustomerDTO cusDTO = customerService.update(customerDTO);
        //getID after employee created -> then pass to employee account

        customerAccountDTO.setId(cusHasAccount.getId());
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
        registerDTO.setFirst_name(customerDTO.getFirst_name());
        registerDTO.setLast_name(customerDTO.getLast_name());
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
