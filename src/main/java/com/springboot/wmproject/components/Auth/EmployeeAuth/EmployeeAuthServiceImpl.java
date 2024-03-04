package com.springboot.wmproject.components.Auth.EmployeeAuth;

import com.springboot.wmproject.DTO.EmployeeAccountDTO;
import com.springboot.wmproject.DTO.EmployeeDTO;
import com.springboot.wmproject.DTO.OrganizeTeamDTO;
import com.springboot.wmproject.components.Auth.DTO.LoginDTO;
import com.springboot.wmproject.components.Auth.DTO.RegisterDTO;
import com.springboot.wmproject.components.Employee.EmployeeAccountService;
import com.springboot.wmproject.components.Employee.EmployeeService;
import com.springboot.wmproject.components.Token.JWT.JwtTokenProvider;
import com.springboot.wmproject.securities.AuthenticationToken.EmployeeUsernamePasswordAuthenticationToken;
import com.springboot.wmproject.securities.UserDetails.CustomUserDetails;
import com.springboot.wmproject.services.OrganizeTeamService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeAuthServiceImpl implements EmployeeAuthService {

    private final AuthenticationManager authenticationManager;
    private final EmployeeAccountService employeeAccountService;
    private final OrganizeTeamService teamService;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;
    private List<String> errors;

    @Override
    public HashMap<String, String> login(LoginDTO loginDTO)
    {
        Authentication authentication = authenticationManager.authenticate(new EmployeeUsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authentication);

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        int id = Math.toIntExact(((CustomUserDetails) authentication.getPrincipal()).getUserId());
        EmployeeAccountDTO employeeAccountDTO = employeeAccountService.getEmployeeAccountByEmployeeId(id);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication, employeeAccountDTO);

        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("refreshToken", refreshToken);
        return map;
    }



    private boolean checkValidEmployeeInformation(RegisterDTO registerDTO) {

        errors = new ArrayList<>();

        Integer teamId = registerDTO.getTeam_id();
        OrganizeTeamDTO teamDTO = teamService.getOneOrganizeTeamById(teamId);
//         String teamName = teamDTO.getTeamName().trim();

        EmployeeDTO employeeDTO = new EmployeeDTO();
        EmployeeAccountDTO employeeAccountDTO = new EmployeeAccountDTO();

        boolean isLeader = registerDTO.isLeader();
        boolean isPhoneValid = employeeService.checkPhoneExists(registerDTO.getPhone());
        boolean isEmailValid = employeeService.checkEmailExists(registerDTO.getEmail());
        boolean isUsernameValid = employeeAccountService.checkUsernameExists(registerDTO.getUsername());
        boolean isPasswordValid = registerDTO.getPassword() != null && !registerDTO.getPassword().isEmpty() && !registerDTO.getPassword().trim().isBlank();

        //VALID PHONE

        if (isPhoneValid) {
            employeeDTO.setPhone(registerDTO.getPhone());
        } else {
            errors.add("Phone number: " + registerDTO.getPhone() + " has already existed");
        }

        //VALID EMAIL

        if (isEmailValid) {
            employeeDTO.setEmail(registerDTO.getEmail().trim());
        } else {
            errors.add("Email Address : " + registerDTO.getEmail() + " has already existed");
        }

        //VALID LEADER

        if (isLeader) {
            List<EmployeeDTO> teamByTeamId = employeeService.findAllByTeamId(teamId);
            if (!teamByTeamId.isEmpty()) {
                boolean isAlreadyLeaderInTeam = teamByTeamId.stream().anyMatch(EmployeeDTO::isLeader);
                if (isAlreadyLeaderInTeam) {
                    errors.add(teamDTO.getTeamName() + " already has a leader");
                } else {
                    employeeDTO.setLeader(true);
                }
            } else {
                // Team has no members
                employeeDTO.setLeader(true);
            }
        } else {
            // isLeader == false
            employeeDTO.setLeader(false);
        }

        //VALID USERNAME
        if (isUsernameValid) {
            employeeAccountDTO.setUsername(registerDTO.getUsername().trim());
        } else {
            errors.add("Username : " + registerDTO.getUsername() + " has already existed");
        }
        //VALID PASSWORD

        if (isPasswordValid) {
            employeeAccountDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        } else {
            errors.add("Password cannot be empty");
        }

        if (errors.size() != 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            String responseError = objectMapper.writeValueAsString(errors);
            throw new WmAPIException(HttpStatus.BAD_REQUEST, responseError);
        }



        employeeDTO.empl

        EmployeeDTO empDTO = employeeService.create(employeeDTO);


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


        return false;
    }

    @Override
    public RegisterDTO register(RegisterDTO registerDTO) {
        //CHECK VALID INFORMATION
        checkValidEmployeeInformation(registerDTO);
        return registerDTO;
    }
}
