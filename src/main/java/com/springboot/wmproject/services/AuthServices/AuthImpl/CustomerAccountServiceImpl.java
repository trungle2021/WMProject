package com.springboot.wmproject.services.AuthServices.AuthImpl;

import com.springboot.wmproject.DTO.CustomerAccountDTO;
import com.springboot.wmproject.DTO.CustomerDTO;
import com.springboot.wmproject.entities.CustomerAccounts;
import com.springboot.wmproject.entities.Customers;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.exceptions.UserNotFoundException;
import com.springboot.wmproject.exceptions.WmAPIException;
import com.springboot.wmproject.repositories.CustomerAccountRepository;
import com.springboot.wmproject.repositories.CustomerRepository;
import com.springboot.wmproject.services.AuthServices.CustomerAccountService;
import com.springboot.wmproject.services.AuthServices.PasswordResetTokenService;
import com.springboot.wmproject.utils.EmailSender;
import com.springboot.wmproject.utils.SD;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerAccountServiceImpl implements CustomerAccountService {
    private CustomerAccountRepository customerAccountRepository;
    private CustomerRepository customerRepository;
    private ModelMapper modelMapper;

    private EmailSender sender;

    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    public CustomerAccountServiceImpl(EmailSender sender,CustomerAccountRepository customerAccountRepository, CustomerRepository customerRepository, ModelMapper modelMapper,PasswordResetTokenService passwordResetTokenService) {
        this.customerAccountRepository = customerAccountRepository;
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.sender = sender;
        this.passwordResetTokenService = passwordResetTokenService;
    }

    @Override
    public List<CustomerAccountDTO> findAll() {
        List<CustomerAccounts> accountsList = customerAccountRepository.findAll();

        return accountsList.stream().map(account -> mapToDto(account)).collect(Collectors.toList());
    }

    @Override
    public CustomerAccountDTO getAccountByAccountId(int id) {
        return null;
    }

    @Override
    public CustomerAccountDTO getAccountByCustomerId(int id) {
        return mapToDto(customerAccountRepository.getCustomerAccountByCustomerId(id));
    }


    @Override
    @Transactional
    public CustomerAccountDTO create(CustomerAccountDTO customerAccountDTO) {

        int customerID = customerAccountDTO.getCustomerId();

        if (customerID != 0) {
            //check if employee exist
            Customers customers = customerRepository.findById(customerID).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", String.valueOf(customerID)));
            //if employee info exist -> able to create account
            if (customers != null) {
                //check if username exist
                Optional<CustomerAccounts> customerAccounts= customerAccountRepository.findByUsername(customerAccountDTO.getUsername());
                if(customerAccounts.isPresent()){
                    throw new WmAPIException(HttpStatus.BAD_REQUEST,"Username already existed");
                }
                return mapToDto(customerAccountRepository.save(mapToEntity(customerAccountDTO)));

            }

        }
        return null;
    }

    @Override
    @Transactional
    public CustomerAccountDTO update(CustomerAccountDTO customerAccountDTO) {
        int customerAccountID = customerAccountDTO.getId();
        if(customerAccountID == 0){
            throw new WmAPIException(HttpStatus.BAD_REQUEST, "CustomerAccount ID is required to update");
        }
        //check employee account exist
//        CustomerAccounts customerAccounts = customerAccountRepository.findById(customerAccountID).orElseThrow(() -> new ResourceNotFoundException("Customer Account", "id", String.valueOf(customerAccountID)));
//        //if exist update
//        customerAccounts.setUsername(customerAccountDTO.getUsername().trim());
//        if(!customerAccountDTO.getPassword().equals("") || !customerAccountDTO.getPassword().isEmpty() || !customerAccountDTO.getPassword().isBlank()){
//            customerAccounts.setPassword(customerAccountDTO.getPassword());
//        }

        CustomerAccounts customerAccounts = customerAccountRepository.findById(customerAccountID).orElseThrow(() -> new ResourceNotFoundException("Customer Account", "id", String.valueOf(customerAccountID)));

        String trimmedUsername = customerAccountDTO.getUsername().trim();
        if (!trimmedUsername.equals(customerAccounts.getUsername())) {
            customerAccounts.setUsername(trimmedUsername);
        }

        String password = customerAccountDTO.getPassword();
        if (password != null && !password.isEmpty()) {
            customerAccounts.setPassword(password);
        }
            return mapToDto(customerAccountRepository.save(customerAccounts));
    }

    @Override
    @Transactional
    public void delete(int id) {
        CustomerAccounts customerAccounts = customerAccountRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("CustomerAccount","id",String.valueOf(id)));
       if(customerAccounts != null){
           customerAccountRepository.delete(customerAccounts);
       }
    }

    @Override
    public CustomerAccountDTO save(CustomerAccountDTO employeeAccountDTO) {
        return null;
    }

    @Override
    public CustomerAccountDTO findByEmail(String email) {
        CustomerAccounts customerAccounts = customerAccountRepository.findByEmail(email);
        if(customerAccounts == null){
            throw new ResourceNotFoundException("CustomerAccount","email",email);
        }
        return mapToDto(customerAccounts);
    }

    @Override
    public List<CustomerAccounts> checkUsernameExists(String username) {
        return customerAccountRepository.checkUsernameExists(username);
    }

    @Override
    public CustomerAccountDTO getByResetPasswordToken(String token) {
        CustomerAccounts customerAccounts= customerAccountRepository.getByResetPasswordToken(token);
        if(customerAccounts == null){
            throw new UserNotFoundException("Invalid Token");
        }
        return mapToDto(customerAccounts);
    }

    @Override
    public String updatePassword(String newPass,String token) throws ParseException {
        CustomerAccountDTO accountDTO = getByResetPasswordToken(token);
        validToken(token);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPass = passwordEncoder.encode(newPass);
        CustomerAccounts accounts = mapToEntity(accountDTO);
        accounts.setPassword(encodedPass);
        passwordResetTokenService.delete(token);
        customerAccountRepository.save(accounts);
        return "Your Password Has Been Updated";
    }




    @Override
    public String updatePasswordMobile(String newPass,String token) throws ParseException {
        CustomerAccountDTO accountDTO = getByResetPasswordToken(token);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPass = passwordEncoder.encode(newPass);
        CustomerAccounts accounts = mapToEntity(accountDTO);
        accounts.setPassword(encodedPass);
        passwordResetTokenService.delete(token);
        customerAccountRepository.save(accounts);
        return "Your Password Has Been Updated";
    }


    @Override
    public String processForgotPassword(String email,String userAgent) {
       CustomerAccountDTO customerAccountDTO = findByEmail(email);
        String recipient = email;
        String subject = "Reset Password - WM RESTAURANT" ;
        String content;
       String tokenCreated;
       if(userAgent.contains("okhttp")){
            tokenCreated = passwordResetTokenService.createTokenMobile(customerAccountDTO.getId());
            content = "<p>Hello,</p>"
                    + "<p>You have requested to reset your password.</p>"
                    + "<p>Use OTP Code below to change your password:</p>"
                    + "<p>"+ tokenCreated +"</p>"
                    + "<br>"
                    + "<p>Ignore this email if you do remember your password, "
                    + "or you have not made the request.</p>";
       }else{
            tokenCreated = passwordResetTokenService.create(customerAccountDTO.getId());
           String link = SD.DOMAIN_APP_CLIENT + "changePassword?token=" + tokenCreated;
            content =
                   "<p>Hello,</p>"
                           + "<p>You have requested to reset your password.</p>"
                           + "<p>Click the link below to change your password:</p>"
                           + "<p><a href=\"" + link + "\">Change my password</a></p>"
                           + "<br>"
                           + "<p>Ignore this email if you do remember your password, "
                           + "or you have not made the request.</p>";
       }
       try{
           sender.sendEmail(recipient,subject,content);
           return tokenCreated;
       }catch(Exception e){
           return "Unable to send email!";
       }
    }

    @Override
    public String sendVerifyEmail(String email,String userAgent) {
        CustomerAccountDTO customerAccountDTO = findByEmail(email);
        String recipient = email;
        String subject = " Email Verification - KTK RESTAURANT " ;
        String content;
        String tokenCreated;
        if(userAgent.contains("okhttp")){
            tokenCreated = passwordResetTokenService.createTokenMobile(customerAccountDTO.getId());
            content = "<p>Hello,</p>"
                    + "<p>Thank you for registering! Please verify your email address by clicking the button below:</p>"
                    + "<p>Use OTP Code below to verify your email:</p>"
                    + "<p>"+ tokenCreated +"</p>"
                    + "<br>"
                    + "<p>Ignore this email if you have not made the request.";

        }else{
            tokenCreated = passwordResetTokenService.create(customerAccountDTO.getId());
            String link = SD.DOMAIN_APP_CLIENT + "sendVerifyEmail?token=" + tokenCreated;
            content =
                    "<p>Hello,</p>"
                            + "<p>Thank you for registering! Please verify your email address by clicking the button below:</p>"
                            + "<p>Click the link below to verify your email:</p>"
                            + "<p><a href=\"" + link + "\">Verify Account</a></p>"
                            + "<br>"
                            + "<p>Ignore this email if you have not made the request.";
        }
        try{
            sender.sendEmail(recipient,subject,content);
            return tokenCreated;
        }catch(Exception e){
            return "Unable to send email!";
        }
    }

    @Override
    public String verifyEmailRegister(String token) throws ParseException {
        CustomerAccountDTO accountDTO = getByResetPasswordToken(token);
        Customers customerDTO = customerRepository.findById(accountDTO.getCustomerId()).orElseThrow(()->new WmAPIException(HttpStatus.BAD_REQUEST,"Token invalid"));
        validToken(token);
        passwordResetTokenService.delete(token);
        customerDTO.set_verified(true);
        customerRepository.save(customerDTO);
        return "Your Email Has Been Verified";
    }

    @Override
    public String validToken(String token) throws ParseException {
        String checkToken =  passwordResetTokenService.validatePasswordResetToken(token);
        if(!checkToken.equals("Valid")){
            throw new WmAPIException(HttpStatus.BAD_REQUEST,checkToken);
        }
        return token;
    }




    public CustomerAccountDTO mapToDto(CustomerAccounts customerAccounts){
        return modelMapper.map(customerAccounts,CustomerAccountDTO.class);
    }

    public CustomerAccounts mapToEntity(CustomerAccountDTO customerAccountDTO){
        return modelMapper.map(customerAccountDTO,CustomerAccounts.class);
    }


}