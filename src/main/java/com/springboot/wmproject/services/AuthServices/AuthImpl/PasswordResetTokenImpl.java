package com.springboot.wmproject.services.AuthServices.AuthImpl;

import com.springboot.wmproject.DTO.CustomerAccountDTO;
import com.springboot.wmproject.DTO.CustomerDTO;
import com.springboot.wmproject.entities.CustomerAccounts;
import com.springboot.wmproject.entities.PasswordResetToken;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.repositories.CustomerAccountRepository;
import com.springboot.wmproject.repositories.PasswordResetTokenRepository;
import com.springboot.wmproject.services.AuthServices.CustomerAccountService;
import com.springboot.wmproject.services.AuthServices.CustomerService;
import com.springboot.wmproject.services.AuthServices.PasswordResetTokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.springboot.wmproject.utils.OTPGenerator.generateOTP;

@Service
public class PasswordResetTokenImpl implements PasswordResetTokenService {
    private PasswordResetTokenRepository passwordResetTokenRepository;
    private CustomerService customerService;
    private CustomerAccountRepository customerAccountRepository;
    int i = 1;

    @Autowired
    public PasswordResetTokenImpl(PasswordResetTokenRepository passwordResetTokenRepository, CustomerService customerService, CustomerAccountRepository customerAccountRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.customerService = customerService;
        this.customerAccountRepository = customerAccountRepository;
    }

    @Override
    @Transactional

    public String create(int customerAccountId) {
        //create token
        String token = UUID.randomUUID().toString();

        LocalDateTime expiryDateTime = LocalDateTime.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String expiry_date = expiryDateTime.format(formatter);

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setCustomerAccountsId(customerAccountId);
        passwordResetToken.setExpiryDate(expiry_date);
        passwordResetTokenRepository.save(passwordResetToken);
        return token;
    }

    @Override
    @Transactional

    public String createTokenMobile(int customerAccountId) {
        //create token
        String token = generateOTP();
//        c56a5c3f-cfaf-487c-ac25-ddcef5ccc5f0

        LocalDateTime expiryDateTime = LocalDateTime.now().plusMinutes(30);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String expiry_date = expiryDateTime.format(formatter);

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setCustomerAccountsId(customerAccountId);
        passwordResetToken.setExpiryDate(expiry_date);
        passwordResetTokenRepository.save(passwordResetToken);
        return token;
    }

    @Override
    @Transactional

    public void delete(String token) {
         PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("PasswordToken","token",token));
        if(resetToken != null){
            passwordResetTokenRepository.delete(resetToken);
        }
    }
    @Override
    public String validatePasswordResetToken(String token) throws ParseException {
          PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("Password Token ","token",token));
         return !isTokenFound(resetToken) ? "Invalid Token" :isTokenExpired(resetToken) ? "Expired Token" :"Valid";
    }

    @Override
    public boolean isTokenFound(PasswordResetToken passToken){
        return passToken != null;
    }


    @Override
    public boolean isTokenExpired(PasswordResetToken passToken) throws ParseException {

        String expiry_date = passToken.getExpiryDate();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date expiryDate = formatter.parse(expiry_date);
        boolean isExpired = expiryDate.before(new Date());

        return isExpired;
    }

    @Override
    public void deleteExpiredTokens() {
        Date currentTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        passwordResetTokenRepository.findAll()
                .stream()
                .forEach(entity -> {
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime expiredDateTime = LocalDateTime.parse(entity.getExpiryDate(), formatter);
                    LocalDateTime newDateTime = currentDateTime;

                    int comparisonResult = expiredDateTime.compareTo(newDateTime);

                    if (comparisonResult < 0) {
                        passwordResetTokenRepository.delete(entity);
                        CustomerDTO customerDTO = customerService.getByCustomerAccountId(entity.getCustomerAccountsId());
                        if(!customerDTO.is_verified()){
                            customerService.delete(customerDTO.getId());
                            System.out.println(newDateTime);
                            System.out.println("Deleted expired token with expiry date: " + entity.getExpiryDate());
                            System.out.println("Deleted customer account has name: " + customerDTO.getFirst_name() + ' ' + customerDTO.getLast_name());
                        }
                    }
                });
    }
}
