package com.springboot.wmproject.services.AuthServices.AuthImpl;

import com.springboot.wmproject.DTO.CustomerAccountDTO;
import com.springboot.wmproject.entities.CustomerAccounts;
import com.springboot.wmproject.entities.PasswordResetToken;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.repositories.PasswordResetTokenRepository;
import com.springboot.wmproject.services.AuthServices.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
public class PasswordResetTokenImpl implements PasswordResetTokenService {
    private PasswordResetTokenRepository passwordResetTokenRepository;


    @Autowired
    public PasswordResetTokenImpl(PasswordResetTokenRepository passwordResetTokenRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    public void create(CustomerAccountDTO accountDTO, String token) {
        CustomerAccounts accounts = new CustomerAccounts();
        accounts.setId(accountDTO.getId());
        accounts.setUsername(accountDTO.getUsername());
        accounts.setPassword(accountDTO.getPassword());
        accounts.setCustomerId(accountDTO.getCustomerId());
        PasswordResetToken passwordResetToken = new PasswordResetToken(accounts,token);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public void delete(String token) {
         PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("PasswordToken","token",token));
        if(resetToken != null){
            passwordResetTokenRepository.delete(resetToken);
        }
    }

    public String validatePasswordResetToken(String token){
          PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("PasswordToken","token",token));
         return !isTokenFound(resetToken) ? "invalidToken"
                 :isTokenExpired(resetToken)?"expired"
                 :null;
    }

    public boolean isTokenFound(PasswordResetToken passToken){
        return passToken != null;
    }


    public boolean isTokenExpired(PasswordResetToken passToken){
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }
}
