package com.springboot.wmproject.components.Auth.Customer;

import com.springboot.wmproject.components.Auth.dto.LoginDTO;
import com.springboot.wmproject.components.Auth.dto.RegisterCustomerDTO;
import com.springboot.wmproject.securities.AuthenticationToken.CustomerUsernamePasswordAuthenticationToken;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
@AllArgsConstructor
public class CustomerAuthServiceImpl implements CustomerAuthService
{
    private final AuthenticationManager authenticationManager;
    private final

        private List<String> errors;
    @Override
    public HashMap<String, String> login(LoginDTO loginDTO) {
                Authentication authentication = authenticationManager
                .authenticate(new CustomerUsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.generateAccessToken(authentication);
        int id = Math.toIntExact(((CustomUserDetails) authentication.getPrincipal()).getUserId());
        CustomerAccountDTO customerAccountDTO = customerAccountService.getAccountByCustomerId(id);

        String refreshToken = tokenProvider.generateRefreshToken(authentication,customerAccountDTO);
        HashMap<String,String> map = new HashMap<>();
        map.put("accessToken",accessToken);
        map.put("refreshToken",refreshToken);
        return map;
    }

    @Override
    public RegisterCustomerDTO register(RegisterCustomerDTO registerDTO) {
        errors = new ArrayList<>();
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
    }
}
