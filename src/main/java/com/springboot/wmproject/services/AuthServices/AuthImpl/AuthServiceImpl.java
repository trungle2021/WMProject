package com.springboot.wmproject.services.AuthServices.AuthImpl;

import com.springboot.wmproject.DTO.*;
//import com.springboot.wmproject.security.AuthenticationToken.CustomerUsernamePasswordAuthenticationToken;
import com.springboot.wmproject.entities.OrganizeTeams;
import com.springboot.wmproject.exceptions.WmAPIException;
import com.springboot.wmproject.repositories.OrganizeTeamRepository;
import com.springboot.wmproject.securities.AuthenticationToken.CustomerUsernamePasswordAuthenticationToken;
import com.springboot.wmproject.securities.AuthenticationToken.EmployeeUsernamePasswordAuthenticationToken;
import com.springboot.wmproject.securities.JWT.JwtTokenProvider;
import com.springboot.wmproject.services.AuthServices.*;
import com.springboot.wmproject.services.OrganizeTeamService;
import com.springboot.wmproject.utils.SD;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    public AuthServiceImpl(OrganizeTeamService teamService, AuthenticationManager authenticationManager, EmployeeService employeeService, CustomerService customerService, EmployeeAccountService employeeAccountService, CustomerAccountService customerAccountService, BCryptPasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider,PasswordResetTokenService passwordResetTokenService) {
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
        OrganizeTeamDTO teamDTO = teamService.getOneOrganizeTeamById(registerDTO.getTeam_id()) ;EmployeeDTO employeeDTO = new EmployeeDTO();
        String teamName = teamDTO.getTeamName();


        employeeDTO.setName(registerDTO.getName());
        employeeDTO.setPhone(registerDTO.getPhone());
        employeeDTO.setSalary(registerDTO.getSalary());
        employeeDTO.setAddress(registerDTO.getAddress());
        employeeDTO.setJoinDate(registerDTO.getJoinDate());
        employeeDTO.setEmail(registerDTO.getEmail());

        if(registerDTO.isLeader()){
            List<EmployeeDTO> empInTeam = employeeService.findAllByTeamId(registerDTO.getTeam_id());
            Boolean hasLeaderInTeam = empInTeam.stream().map(emp -> emp.isLeader()).findFirst().isPresent();
            if(!hasLeaderInTeam){
                employeeDTO.setLeader(true);
            }else{
                throw new WmAPIException(HttpStatus.BAD_REQUEST,"Team" + teamDTO.getTeamName() + "already has a leader");
            }
        }
        employeeDTO.setTeam_id(registerDTO.getTeam_id());
        employeeDTO.setAvatar(registerDTO.getAvatar());
        EmployeeDTO empDTO = employeeService.create(employeeDTO);
        //getID after employee created -> then pass to employee account

        EmployeeAccountDTO employeeAccountDTO = new EmployeeAccountDTO();
        employeeAccountDTO.setUsername(registerDTO.getUsername());
        employeeAccountDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        String role = "";

        if(teamName.equals(SD.TEAM_ADMINISTRATOR)){
            role = SD.ROLE_SALE;
        }else{
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
    public RegisterCustomerDTO customerRegister(RegisterCustomerDTO registerDTO) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstname(registerDTO.getFirstname());
        customerDTO.setLastname(registerDTO.getLastname());
        customerDTO.setPhone(registerDTO.getPhone());
        customerDTO.setAddress(registerDTO.getAddress());
        customerDTO.setGender(registerDTO.getGender());
        customerDTO.setAvatar(registerDTO.getAvatar());
        customerDTO.setEmail(registerDTO.getEmail());
        CustomerDTO cusDTO = customerService.create(customerDTO);
        //getID after employee created -> then pass to employee account

        CustomerAccountDTO customerAccountDTO = new CustomerAccountDTO();
        customerAccountDTO.setUsername(registerDTO.getUsername());
        customerAccountDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        customerAccountDTO.setCustomerId(cusDTO.getId());
        customerAccountService.create(customerAccountDTO);
        registerDTO.setCustomerId(cusDTO.getId());
//        registerDTO.setPassword(customerAccountDTO.getPassword());
        return registerDTO;
    }


}
