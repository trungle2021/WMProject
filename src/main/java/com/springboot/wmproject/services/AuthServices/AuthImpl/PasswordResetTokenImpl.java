package com.springboot.wmproject.services.AuthServices.AuthImpl;

import com.springboot.wmproject.DTO.CustomerAccountDTO;
import com.springboot.wmproject.entities.CustomerAccounts;
import com.springboot.wmproject.entities.PasswordResetToken;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.repositories.PasswordResetTokenRepository;
import com.springboot.wmproject.services.AuthServices.CustomerAccountService;
import com.springboot.wmproject.services.AuthServices.PasswordResetTokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetTokenImpl implements PasswordResetTokenService {
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    public PasswordResetTokenImpl( PasswordResetTokenRepository passwordResetTokenRepository,ModelMapper modelMapper) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    @Transactional

    public String create(int customerAccountId) {
        //create token
        String token = UUID.randomUUID().toString();

        LocalDateTime expiryDateTime = LocalDateTime.now().plusMinutes(60*24);
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
          PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("PasswordToken","token",token));
         return !isTokenFound(resetToken) ? "Invalid Token" :isTokenExpired(resetToken) ? "Expired" :"Valid";
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
}
