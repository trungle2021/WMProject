//package com.springboot.wmproject.components.Auth.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.springboot.wmproject.DTO.*;
//import com.springboot.wmproject.components.Auth.dto.LoginDTO;
//import com.springboot.wmproject.components.Auth.dto.RegisterCustomerDTO;
//import com.springboot.wmproject.components.Auth.dto.RegisterDTO;
//import com.springboot.wmproject.components.Auth.RefreshTokenService;
//import com.springboot.wmproject.components.Employee.EmployeeAccountService;
//import com.springboot.wmproject.components.Employee.EmployeeService;
//import com.springboot.wmproject.components.Customer.CustomerAccountService;
//import com.springboot.wmproject.components.Customer.CustomerService;
//import com.springboot.wmproject.exceptions.RefreshTokenNotFoundException;
//import com.springboot.wmproject.exceptions.WmAPIException;
//import com.springboot.wmproject.securities.AuthenticationToken.CustomerUsernamePasswordAuthenticationToken;
//import com.springboot.wmproject.securities.AuthenticationToken.EmployeeUsernamePasswordAuthenticationToken;
//import com.springboot.wmproject.securities.JWT.JwtTokenProvider;
//import com.springboot.wmproject.securities.UserDetails.CustomUserDetails;
//import com.springboot.wmproject.services.AuthServices.*;
//import com.springboot.wmproject.services.OrderService;
//import com.springboot.wmproject.services.OrganizeTeamService;
//import com.springboot.wmproject.utils.SD;
//import lombok.AllArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.*;
//
//@Service
//@AllArgsConstructor
//public class AuthServiceImpl implements AuthService {
//
//    private final AuthenticationManager authenticationManager;
//    private final EmployeeService employeeService;
//    private final CustomerService customerService;
//    private final EmployeeAccountService employeeAccountService;
//    private final CustomerAccountService customerAccountService;
//    private final BCryptPasswordEncoder passwordEncoder;
//    private final JwtTokenProvider tokenProvider;
//    private final OrganizeTeamService teamService;
//    private final RefreshTokenService refreshTokenService;
//    private final OrderService orderService;
//    private  List<String> errors;
//
//    @Override
//    public String findRoleByEmployeeID(int empID) {
//        return employeeService.findRoleByEmployeeID(empID);
//    }
//
//    @Override
//    @Transactional
//    public String staffDelete(int id) {
//        EmployeeAccountDTO employeeAccountDTO =  employeeAccountService.getEmployeeAccountByEmployeeId(id);
//        employeeService.softDelete(id);
//        employeeAccountService.delete(employeeAccountDTO.getId());
//        return "Employee Deleted Successfully ";
//    }
//
//    @Override
//    public HashMap<String, String> employeeLogin(LoginDTO loginDTO) {
//        Authentication authentication = authenticationManager
//                .authenticate(new EmployeeUsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
//
//        SecurityContext sc = SecurityContextHolder.getContext();
//        sc.setAuthentication(authentication);
//
//        String accessToken = tokenProvider.generateAccessToken(authentication);
//        int id = Math.toIntExact(((CustomUserDetails) authentication.getPrincipal()).getUserId());
//        EmployeeAccountDTO employeeAccountDTO = employeeAccountService.getEmployeeAccountByEmployeeId(id);
//        String refreshToken = tokenProvider.generateRefreshToken(authentication,employeeAccountDTO);
//
//        HashMap<String,String> map = new HashMap<>();
//        map.put("accessToken",accessToken);
//        map.put("refreshToken",refreshToken);
//        return map;
//    }
//
//    @Override
//    public HashMap<String, String> customerLogin(LoginDTO loginDTO) {
//        Authentication authentication = authenticationManager
//                .authenticate(new CustomerUsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String accessToken = tokenProvider.generateAccessToken(authentication);
//        int id = Math.toIntExact(((CustomUserDetails) authentication.getPrincipal()).getUserId());
//        CustomerAccountDTO customerAccountDTO = customerAccountService.getAccountByCustomerId(id);
//
//        String refreshToken = tokenProvider.generateRefreshToken(authentication,customerAccountDTO);
//        HashMap<String,String> map = new HashMap<>();
//        map.put("accessToken",accessToken);
//        map.put("refreshToken",refreshToken);
//        return map;
//    }
//
//    @Override
//    public RegisterDTO employeeRegister(RegisterDTO registerDTO) throws JsonProcessingException {
//        errors = new ArrayList<>();
//        //valid team_id, if not found team by ID throw 404
//        Integer team_id = registerDTO.getTeam_id() == null ? 0 : registerDTO.getTeam_id();
//        OrganizeTeamDTO teamDTO = teamService.getOneOrganizeTeamById(team_id);
//        String teamName = teamDTO.getTeamName().trim();
//
//        EmployeeDTO employeeDTO = new EmployeeDTO();
//        EmployeeAccountDTO employeeAccountDTO = new EmployeeAccountDTO();
//
//        Integer isLeader = registerDTO.getIsLeader() == null ? 0 : registerDTO.getIsLeader();
//
//
//
//        boolean isValidPhone = employeeService.checkPhoneExists(registerDTO.getPhone()).size() == 0;
//        boolean isValidEmail = employeeService.checkEmailExists(registerDTO.getEmail()).size() == 0;
//        boolean isValidUsername = employeeAccountService.checkUsernameExists(registerDTO.getUsername()).size() == 0;
//        boolean isValidPassword = registerDTO.getPassword() != null && !registerDTO.getPassword().isEmpty() && !registerDTO.getPassword().trim().isBlank();
//
//        //VALID PHONE
//
//        if (isValidPhone) {
//            employeeDTO.setPhone(registerDTO.getPhone());
//        } else {
//            errors.add("Phone number: " + registerDTO.getPhone() + " has already existed");
//        }
//
//        //VALID EMAIL
//
//        if (isValidEmail) {
//            employeeDTO.setEmail(registerDTO.getEmail().trim());
//        } else {
//            errors.add("Email Address : " + registerDTO.getEmail() + " has already existed");
//        }
//
//        //VALID LEADER
//
//        if (isLeader == 1) {
//            List<EmployeeDTO> allEmpInTeam = employeeService.findAllByTeamId(isLeader);
//            if(allEmpInTeam != null){
//                Boolean hasLeaderInTeam = allEmpInTeam.stream().filter(emp -> emp.getIsLeader() == 1).findFirst().isPresent();
//                if (!hasLeaderInTeam) {
//                    employeeDTO.setIsLeader(1);
//                } else {
//                    errors.add(teamDTO.getTeamName() + " already has a leader");
//                }
//            } else{
//                // Team has no members
//                employeeDTO.setIsLeader(1);
//            }
//        }else{
//            // isLeader == 0
//            employeeDTO.setIsLeader(isLeader);
//        }
//
//        //VALID USERNAME
//        if (isValidUsername) {
//            employeeAccountDTO.setUsername(registerDTO.getUsername().trim());
//        } else {
//            errors.add("Username : " + registerDTO.getUsername() + " has already existed");
//        }
//        //VALID PASSWORD
//
//        if(isValidPassword){
//            employeeAccountDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
//        } else {
//            errors.add("Password cannot be empty");
//        }
//
//        if (errors.size() != 0) {
//            ObjectMapper objectMapper = new ObjectMapper();
//            String responseError = objectMapper.writeValueAsString(errors);
//            throw new WmAPIException(HttpStatus.BAD_REQUEST, responseError);
//        }
//
//        employeeDTO.setName(registerDTO.getName().trim());
//        employeeDTO.setSalary(registerDTO.getSalary());
//        employeeDTO.setAddress(registerDTO.getAddress().trim());
//        employeeDTO.setJoinDate(registerDTO.getJoinDate());
//        employeeDTO.setTeam_id(registerDTO.getTeam_id());
//        employeeDTO.setGender(registerDTO.getGender());
//        employeeDTO.setAvatar(registerDTO.getAvatar());
//        employeeDTO.set_deleted(false);
//        EmployeeDTO empDTO = employeeService.create(employeeDTO);
//        //getID after employee created -> then pass to employee account
//
//        String role;
//
//        if (teamName.equals(SD.TEAM_ADMINISTRATOR)) {
//            role = SD.ROLE_SALE;
//        } else {
//            role = SD.ROLE_ORGANIZE;
//        }
//        employeeAccountDTO.setRole(role);
//        employeeAccountDTO.setEmployeeId(empDTO.getId());
//        employeeAccountService.create(employeeAccountDTO);
//        registerDTO.setEmployeeId(empDTO.getId());
//        registerDTO.setRole(role);
//
//        return registerDTO;
//    }
//
//    @Override
//    public RegisterCustomerDTO customerRegister(RegisterCustomerDTO registerDTO, String userAgent) throws JsonProcessingException {
//        errors = new ArrayList<>();
//        boolean isPhoneValid = customerService.checkPhoneExists(registerDTO.getPhone()).size() == 0;
//        boolean isEmailValid = customerService.checkEmailExists(registerDTO.getEmail()).size() == 0;
//        boolean isUsernameValid = customerAccountService.checkUsernameExists(registerDTO.getUsername()).size() == 0;
//        boolean isValidPassword = registerDTO.getPassword() != null && !registerDTO.getPassword().isEmpty() && !registerDTO.getPassword().isBlank();
//
//        CustomerDTO customerDTO = new CustomerDTO();
//        CustomerAccountDTO customerAccountDTO = new CustomerAccountDTO();
//
//        //valid
//        if (isPhoneValid) {
//            customerDTO.setPhone(registerDTO.getPhone());
//        } else {
//            errors.add("Phone number: " + registerDTO.getPhone() + " has already existed");
//        }
//        if (isEmailValid) {
//            customerDTO.setEmail(registerDTO.getEmail().trim());
//        } else {
//            errors.add("Email Address : " + registerDTO.getEmail() + " has already existed");
//        }
//
//        if (isUsernameValid) {
//            customerAccountDTO.setUsername(registerDTO.getUsername());
//        } else {
//            errors.add("Username : " + registerDTO.getUsername() + " has already existed");
//        }
//
//        if(isValidPassword){
//            customerAccountDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
//        } else {
//            errors.add("Password cannot be empty");
//        }
//
//        if (errors.size() != 0) {
//            ObjectMapper objectMapper = new ObjectMapper();
//            String responseError = objectMapper.writeValueAsString(errors);
//            throw new WmAPIException(HttpStatus.BAD_REQUEST, responseError);
//        }
//
//        customerDTO.setFirst_name(registerDTO.getFirst_name().trim());
//        customerDTO.setLast_name(registerDTO.getLast_name().trim());
//        customerDTO.setAddress(registerDTO.getAddress().trim());
//        customerDTO.setGender(registerDTO.getGender());
//        customerDTO.setAvatar(registerDTO.getAvatar());
//        CustomerDTO cusDTO = customerService.create(customerDTO);
//        //getID after employee created -> then pass to employee account
//
//        customerAccountDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
//        customerAccountDTO.setCustomerId(cusDTO.getId());
//        customerAccountService.create(customerAccountDTO);
//        registerDTO.setCustomerId(cusDTO.getId());
//
//        customerAccountService.sendVerifyEmail(registerDTO.getEmail(),userAgent);
//
//
//
//        return registerDTO;
//    }
//
//    @Override
//    public RegisterCustomerDTO customerPersonalValid(RegisterCustomerDTO registerDTO) throws JsonProcessingException {
//        errors = new ArrayList<>();
//        boolean isPhoneValid = customerService.checkPhoneExists(registerDTO.getPhone()).size() == 0;
//        boolean isEmailValid = customerService.checkEmailExists(registerDTO.getEmail()).size() == 0;
//
//
//        //valid
//        if (!isPhoneValid) {
//            errors.add("Phone number: " + registerDTO.getPhone() + " has already existed");
//        }
//        if (!isEmailValid) {
//            errors.add("Email Address : " + registerDTO.getEmail() + " has already existed");
//        }
//
//        if (errors.size() != 0) {
//            ObjectMapper objectMapper = new ObjectMapper();
//            String responseError = objectMapper.writeValueAsString(errors);
//            throw new WmAPIException(HttpStatus.BAD_REQUEST, responseError);
//        }
//        return registerDTO;
//    }
//
//    @Override
//public RegisterDTO employeeUpdate(RegisterDTO registerDTO) throws JsonProcessingException {
//        String teamName = "";
//        OrganizeTeamDTO teamDTO;
//        Integer team_id = registerDTO.getTeam_id() == null ? 0 : registerDTO.getTeam_id();
//        Integer isLeader = registerDTO.getIsLeader() == null ? 0 : registerDTO.getIsLeader();
//
//
//        //check emp exist by id
//        EmployeeDTO empExist = employeeService.getEmployeeById(registerDTO.getEmployeeId());
//        EmployeeAccountDTO empHasAccount = employeeAccountService.getEmployeeAccountByEmployeeId(registerDTO.getEmployeeId());
//
//
//
//        errors = new ArrayList<>();
//        EmployeeDTO employeeDTO = new EmployeeDTO();
//        EmployeeAccountDTO employeeAccountDTO = new EmployeeAccountDTO();
//
//        if(team_id != 0){
//             teamDTO = teamService.getOneOrganizeTeamById(team_id);
//            teamName = teamDTO.getTeamName().trim();
//            employeeDTO.setTeam_id(team_id);
//        }else{
//            teamDTO = teamService.getOneOrganizeTeamById(empExist.getTeam_id());
//            teamName = teamDTO.getTeamName().trim();
//            employeeDTO.setTeam_id(teamDTO.getId());
//        }
//
//
//        //Validate
//        boolean isValidPhone = employeeService.checkPhoneExists(registerDTO.getPhone()).size() == 0;
//        boolean isValidEmail = employeeService.checkEmailExists(registerDTO.getEmail()).size() == 0;
//        boolean isValidUsername = employeeAccountService.checkUsernameExists(registerDTO.getUsername()).size() == 0;
//        boolean isValidPassword = registerDTO.getPassword() != null && !registerDTO.getPassword().isEmpty() && !registerDTO.getPassword().trim().isBlank();
//        boolean phoneHasChanged = !empExist.getPhone().equals(registerDTO.getPhone());
//        boolean emailHasChanged = !empExist.getEmail().equalsIgnoreCase(registerDTO.getEmail().trim());
//        boolean userNameHasChanged = !empHasAccount.getUsername().equalsIgnoreCase(registerDTO.getUsername());
//        boolean leaderHasChanged = empExist.getIsLeader() != isLeader;
//
//
//        if(phoneHasChanged){
//            if (isValidPhone) {
//                employeeDTO.setPhone(registerDTO.getPhone());
//            } else {
//                errors.add("Phone number: " + registerDTO.getPhone() + " has already existed");
//            }
//        }else{
//            employeeDTO.setPhone(registerDTO.getPhone());
//        }
//
//        if(emailHasChanged){
//            if (isValidEmail) {
//                employeeDTO.setEmail(registerDTO.getEmail().trim());
//            } else {
//                errors.add("Email Address : " + registerDTO.getEmail() + " has already existed");
//            }
//        }else{
//            employeeDTO.setEmail(registerDTO.getEmail().trim());
//        }
//
//       if(leaderHasChanged){
//           if (isLeader == 1 ) {
//               List<EmployeeDTO> allEmpInTeam = employeeService.findAllByTeamId(team_id);
//               if(allEmpInTeam != null){
//                   Boolean hasLeaderInTeam = allEmpInTeam.stream().filter(emp -> emp.getIsLeader() == 1).findFirst().isPresent();
//                   if (!hasLeaderInTeam) {
//                       employeeDTO.setIsLeader(1);
//                   } else {
//                       errors.add(teamDTO.getTeamName() + " already has a leader");
//                   }
//               }else{
//                   employeeDTO.setIsLeader(1);
//               }
//           }
//       }else{
//           employeeDTO.setIsLeader(isLeader);
//       }
//
//        if(userNameHasChanged){
//            if (isValidUsername) {
//                employeeAccountDTO.setUsername(registerDTO.getUsername().trim());
//            } else {
//                errors.add("Username : " + registerDTO.getUsername() + " has already existed");
//            }
//        }else{
//            employeeAccountDTO.setUsername(empHasAccount.getUsername().trim());
//        }
//
//        if(isValidPassword){
//            employeeAccountDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
//        }else{
//            employeeAccountDTO.setPassword(empHasAccount.getPassword());
//        }
//
//        int currentTeam = empExist.getTeam_id();
//        int teamSize = teamService.getOneOrganizeTeamById(currentTeam).getTeamsize().intValue();
//        List<OrderDTO> orderByTeam = orderService.getAllByOrganizeTeam(currentTeam);
//
//        boolean isAlreadyWorkShift = false;
//                if(orderByTeam != null){
//                     isAlreadyWorkShift = orderByTeam.stream()
//                            .anyMatch(orderDTO -> orderDTO.getOrderStatus().equals(SD.orderStatusConfirm));
//                }
//
//        if(teamSize == 1 && isAlreadyWorkShift){
//            errors.add("Cannot remove all member if team already has work shift");
//        }
//
//        if (errors.size() != 0) {
//            ObjectMapper objectMapper = new ObjectMapper();
//            String responseError = objectMapper.writeValueAsString(errors);
//            throw new WmAPIException(HttpStatus.BAD_REQUEST, responseError);
//        }
//
//        employeeDTO.setId(registerDTO.getEmployeeId());
//        employeeDTO.setName(registerDTO.getName().trim());
//        employeeDTO.setAddress(registerDTO.getAddress().trim());
//        employeeDTO.setJoinDate(registerDTO.getJoinDate());
//        employeeDTO.setSalary(registerDTO.getSalary());
//        employeeDTO.setIsLeader(isLeader);
//
//
//
//        employeeDTO.setGender(registerDTO.getGender());
//        employeeDTO.setAvatar(registerDTO.getAvatar());
//        employeeDTO.setId(empExist.getId());
//        EmployeeDTO empDTO = employeeService.update(employeeDTO);
//        //getID after employee created -> then pass to employee account
//
//        String role = "";
//
//        if (teamName.equals(SD.TEAM_ADMINISTRATOR)) {
//            if(empHasAccount.getRole().equals(SD.ROLE_ADMIN)){
//                role = SD.ROLE_ADMIN;
//            }else{
//                role = SD.ROLE_SALE;
//            }
//        } else {
//            role = SD.ROLE_ORGANIZE;
//        }
//
//        employeeAccountDTO.setId(empHasAccount.getId());
//        employeeAccountDTO.setRole(role);
//        employeeAccountDTO.setEmployeeId(empDTO.getId());
//        employeeAccountService.update(employeeAccountDTO);
//        registerDTO.setEmployeeId(empDTO.getId());
//        registerDTO.setRole(role);
//
//        return registerDTO;
//    }
//
//    @Override
//    public RegisterCustomerDTO customerUpdate(RegisterCustomerDTO registerDTO,String userAgent) throws JsonProcessingException {
//        errors = new ArrayList<>();
//        CustomerDTO customerDTO = new CustomerDTO();
//        CustomerAccountDTO customerAccountDTO = new CustomerAccountDTO();
//        //check cus exist by id
//        CustomerDTO cusExist = customerService.getCustomerById(registerDTO.getCustomerId());
//        CustomerAccountDTO cusHasAccount = customerAccountService.getAccountByCustomerId(registerDTO.getCustomerId());
//
//        //Validate
//        boolean isValidPhone = customerService.checkPhoneExists(registerDTO.getPhone().trim()).size() == 0;
//        boolean isValidEmail = customerService.checkEmailExists(registerDTO.getEmail().trim()).size() == 0;
//        boolean isValidUsername = customerAccountService.checkUsernameExists(registerDTO.getUsername().trim()).size() == 0;
//        boolean isValidPassword = registerDTO.getPassword() != null && !registerDTO.getPassword().isEmpty() && !registerDTO.getPassword().isBlank();
//        boolean phoneHasChanged = !cusExist.getPhone().equals(registerDTO.getPhone());
//        boolean emailHasChanged = !cusExist.getEmail().equalsIgnoreCase(registerDTO.getEmail().trim());
//        boolean userNameHasChanged = !cusHasAccount.getUsername().equalsIgnoreCase(registerDTO.getUsername().trim());
//
//
//        if(phoneHasChanged){
//            if (isValidPhone) {
//                customerDTO.setPhone(registerDTO.getPhone());
//            } else {
//                errors.add("Phone number: " + registerDTO.getPhone() + " has already existed");
//            }
//        }else{
//            customerDTO.setPhone(registerDTO.getPhone());
//        }
//
//        if(emailHasChanged){
//            if (isValidEmail) {
//                customerDTO.setEmail(registerDTO.getEmail().trim());
//            } else {
//                errors.add("Email Address : " + registerDTO.getEmail() + " has already existed");
//            }
//        }else{
//            customerDTO.setEmail(registerDTO.getEmail().trim());
//        }
//
//        if(userNameHasChanged){
//            if (isValidUsername) {
//                customerAccountDTO.setUsername(registerDTO.getUsername().trim());
//            } else {
//                errors.add("Username : " + registerDTO.getUsername() + " has already existed");
//            }
//        }else{
//            customerAccountDTO.setUsername(cusHasAccount.getUsername().trim());
//        }
//
//        if(isValidPassword){
//            customerAccountDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
//        }else{
//            customerAccountDTO.setPassword(cusHasAccount.getPassword());
//        }
//
//        if (errors.size() != 0) {
//            ObjectMapper objectMapper = new ObjectMapper();
//            String responseError = objectMapper.writeValueAsString(errors);
//            throw new WmAPIException(HttpStatus.BAD_REQUEST, responseError);
//        }
//
//        customerDTO.setFirst_name(registerDTO.getFirst_name());
//        customerDTO.setLast_name(registerDTO.getLast_name());
//        customerDTO.setAddress(registerDTO.getAddress());
//        customerDTO.setGender(registerDTO.getGender());
//        customerDTO.setAvatar(registerDTO.getAvatar());
//        customerDTO.setId(cusExist.getId());
//        CustomerDTO cusDTO = customerService.update(customerDTO);
//        //getID after employee created -> then pass to employee account
//
//        customerAccountDTO.setId(cusHasAccount.getId());
//        customerAccountDTO.setCustomerId(cusDTO.getId());
//        customerAccountService.update(customerAccountDTO);
//        registerDTO.setCustomerId(cusDTO.getId());
//        registerDTO.setAvatar(cusDTO.getAvatar());
//        return registerDTO;
//    }
//
//    @Override
//    public RegisterDTO getOneRegisterEmp(int empID) throws JsonProcessingException {
//        EmployeeDTO employeeDTO = employeeService.getEmployeeById(empID);
//        EmployeeAccountDTO employeeAccountDTO = employeeAccountService.getEmployeeAccountByEmployeeId(empID);
//        RegisterDTO registerDTO = new RegisterDTO();
//        registerDTO.setId(employeeDTO.getId());
//        registerDTO.setName(employeeDTO.getName());
//        registerDTO.setAddress(employeeDTO.getAddress());
//        registerDTO.setPhone(employeeDTO.getPhone());
//        registerDTO.setJoinDate(employeeDTO.getJoinDate());
//        registerDTO.setSalary(employeeDTO.getSalary());
//        registerDTO.setEmail(employeeDTO.getEmail());
//        registerDTO.setIsLeader(employeeDTO.getIsLeader());
//        registerDTO.setTeam_id(employeeDTO.getOrganizeTeamsByTeamId().getId());
//        registerDTO.setGender(employeeDTO.getGender());
//        registerDTO.setAvatar(employeeDTO.getAvatar());
//        registerDTO.setUsername(employeeAccountDTO.getUsername());
//        registerDTO.setRole(employeeAccountDTO.getRole());
//        registerDTO.setEmployeeId(empID);
//        return registerDTO;
//    }
//
//    @Override
//    public RegisterCustomerDTO getOneRegisterCustomer(int customerID) throws JsonProcessingException {
//        CustomerDTO customerDTO = customerService.getCustomerById(customerID);
//        CustomerAccountDTO customerAccountDTO = customerAccountService.getAccountByCustomerId(customerID);
//        RegisterCustomerDTO registerDTO = new RegisterCustomerDTO();
//        registerDTO.setId(customerDTO.getId());
//        registerDTO.setFirst_name(customerDTO.getFirst_name());
//        registerDTO.setLast_name(customerDTO.getLast_name());
//        registerDTO.setAddress(customerDTO.getAddress());
//        registerDTO.setPhone(customerDTO.getPhone());
//        registerDTO.setEmail(customerDTO.getEmail());
//        registerDTO.setGender(customerDTO.getGender());
//        registerDTO.setAvatar(customerDTO.getAvatar());
//        registerDTO.setUsername(customerAccountDTO.getUsername());
//        registerDTO.setCustomerId(customerID);
//        return registerDTO;
//    }
//
//    @Override
//    public String refreshToken(String refreshToken) {
//        RefreshTokenDTO refreshTokenDTO = refreshTokenService.getOneByRefreshToken(refreshToken);
//        //check refresh token in DB is expired or not ?
//        if (refreshTokenDTO != null) {
//            boolean refreshTokenIsValid = tokenProvider.validateToken(refreshToken);
//            //if valid
//            if (refreshTokenIsValid) {
//                if (refreshTokenDTO.getEmployeeId() != null) {
//                    EmployeeAccountDTO employeeAccountDTO = employeeAccountService.getEmployeeAccountByEmployeeAccountId(refreshTokenDTO.getEmployeeId());
//
//                    Set<GrantedAuthority> authorities = new HashSet<>();
//                    authorities.add(new SimpleGrantedAuthority(employeeAccountDTO.getRole()));
//
//                    long userid = Long.parseLong(tokenProvider.getUserID(refreshToken));
//                    String username = tokenProvider.getUsername(refreshToken);
//                    String password = employeeAccountDTO.getPassword();
//
//                    CustomUserDetails customUserDetails = new CustomUserDetails(username,password,userid,true,authorities);
//                    Authentication authentication = new EmployeeUsernamePasswordAuthenticationToken(customUserDetails,null,authorities);
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//
//                    String newAccessToken = tokenProvider.generateAccessToken(authentication);
//                    return newAccessToken;
//                } else {
//                    CustomerAccountDTO customerAccountDTO = customerAccountService.getAccountByAccountId(refreshTokenDTO.getCustomerId());
//                    long userid = Long.parseLong(tokenProvider.getUserID(refreshToken));
//                    String role = tokenProvider.getUserType(refreshToken);
//                    String username = tokenProvider.getUsername(refreshToken);
//                    String password = customerAccountDTO.getPassword();
//                    boolean isVerified = Boolean.parseBoolean(tokenProvider.getIsVerified(refreshToken));
//                    Set<GrantedAuthority> authorities = new HashSet<>();
//                    authorities.add(new SimpleGrantedAuthority(role));
//
//                    CustomUserDetails customUserDetails = new CustomUserDetails(username,password,userid,isVerified,authorities);
//                    Authentication authentication = new CustomerUsernamePasswordAuthenticationToken(customUserDetails,null,authorities);
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                    String newAccessToken = tokenProvider.generateAccessToken(authentication);
//                    return newAccessToken;
//                }
//            }else{
//                refreshTokenService.delete(refreshToken);
//                throw new RefreshTokenNotFoundException("Refresh token has expired");
//            }
//        }
//        return null;
//    }
//
//}
