 package com.springboot.wmproject.components.Auth.Employee;

 import com.springboot.wmproject.components.Auth.dto.LoginDTO;
 import com.springboot.wmproject.components.Auth.dto.RegisterDTO;
 import com.springboot.wmproject.securities.AuthenticationToken.EmployeeUsernamePasswordAuthenticationToken;
 import lombok.AllArgsConstructor;
 import org.springframework.security.authentication.AuthenticationManager;
 import org.springframework.security.core.Authentication;
 import org.springframework.security.core.context.SecurityContext;
 import org.springframework.security.core.context.SecurityContextHolder;
 import org.springframework.stereotype.Service;

 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;

 @Service
 @AllArgsConstructor
 public class EmployeeAuthServiceImpl implements EmployeeAuthService{

     private final AuthenticationManager authenticationManager;

         private List<String> errors;
     @Override
     public HashMap<String, String> login(LoginDTO loginDTO) {
                 Authentication authentication = authenticationManager
                 .authenticate(new EmployeeUsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

         SecurityContext sc = SecurityContextHolder.getContext();
         sc.setAuthentication(authentication);

         String accessToken = tokenProvider.generateAccessToken(authentication);
         int id = Math.toIntExact(((CustomUserDetails) authentication.getPrincipal()).getUserId());
         EmployeeAccountDTO employeeAccountDTO = employeeAccountService.getEmployeeAccountByEmployeeId(id);
         String refreshToken = tokenProvider.generateRefreshToken(authentication,employeeAccountDTO);

         HashMap<String,String> map = new HashMap<>();
         map.put("accessToken",accessToken);
         map.put("refreshToken",refreshToken);
         return map;
     }

     @Override
     public RegisterDTO register(RegisterDTO registerDTO) {
         errors = new ArrayList<>();
         Integer team_id = registerDTO.getTeam_id();
         OrganizeTeamDTO teamDTO = teamService.getOneOrganizeTeamById(team_id);
         String teamName = teamDTO.getTeamName().trim();

         EmployeeDTO employeeDTO = new EmployeeDTO();
         EmployeeAccountDTO employeeAccountDTO = new EmployeeAccountDTO();

         Integer isLeader = registerDTO.getIsLeader() == null ? 0 : registerDTO.getIsLeader();



         boolean isValidPhone = employeeService.checkPhoneExists(registerDTO.getPhone()).size() == 0;
         boolean isValidEmail = employeeService.checkEmailExists(registerDTO.getEmail()).size() == 0;
         boolean isValidUsername = employeeAccountService.checkUsernameExists(registerDTO.getUsername()).size() == 0;
         boolean isValidPassword = registerDTO.getPassword() != null && !registerDTO.getPassword().isEmpty() && !registerDTO.getPassword().trim().isBlank();

         //VALID PHONE

         if (isValidPhone) {
             employeeDTO.setPhone(registerDTO.getPhone());
         } else {
             errors.add("Phone number: " + registerDTO.getPhone() + " has already existed");
         }

         //VALID EMAIL

         if (isValidEmail) {
             employeeDTO.setEmail(registerDTO.getEmail().trim());
         } else {
             errors.add("Email Address : " + registerDTO.getEmail() + " has already existed");
         }

         //VALID LEADER

         if (isLeader == 1) {
             List<EmployeeDTO> allEmpInTeam = employeeService.findAllByTeamId(isLeader);
             if(allEmpInTeam != null){
                 Boolean hasLeaderInTeam = allEmpInTeam.stream().filter(emp -> emp.getIsLeader() == 1).findFirst().isPresent();
                 if (!hasLeaderInTeam) {
                     employeeDTO.setIsLeader(1);
                 } else {
                     errors.add(teamDTO.getTeamName() + " already has a leader");
                 }
             } else{
                 // Team has no members
                 employeeDTO.setIsLeader(1);
             }
         }else{
             // isLeader == 0
             employeeDTO.setIsLeader(isLeader);
         }

         //VALID USERNAME
         if (isValidUsername) {
             employeeAccountDTO.setUsername(registerDTO.getUsername().trim());
         } else {
             errors.add("Username : " + registerDTO.getUsername() + " has already existed");
         }
         //VALID PASSWORD

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

         employeeDTO.setName(registerDTO.getName().trim());
         employeeDTO.setSalary(registerDTO.getSalary());
         employeeDTO.setAddress(registerDTO.getAddress().trim());
         employeeDTO.setJoinDate(registerDTO.getJoinDate());
         employeeDTO.setTeam_id(registerDTO.getTeam_id());
         employeeDTO.setGender(registerDTO.getGender());
         employeeDTO.setAvatar(registerDTO.getAvatar());
         employeeDTO.set_deleted(false);
         EmployeeDTO empDTO = employeeService.create(employeeDTO);
         //getID after employee created -> then pass to employee account

         String role;

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
 }
