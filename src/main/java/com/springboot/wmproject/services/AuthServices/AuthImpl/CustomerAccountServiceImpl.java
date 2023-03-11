package com.springboot.wmproject.services.AuthServices.AuthImpl;

import com.springboot.wmproject.DTO.CustomerAccountDTO;
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
@Transactional
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
    public CustomerAccountDTO update(CustomerAccountDTO customerAccountDTO) {
        int customerID = customerAccountDTO.getCustomerId();
        //check employee account exist
        CustomerAccounts customerAccounts = customerAccountRepository.findById(customerID).orElseThrow(() -> new ResourceNotFoundException("Customer Account", "id", String.valueOf(customerID)));
        //if exist update
        if (customerAccounts != null) {
            CustomerAccounts updateCustomer = new CustomerAccounts();
            updateCustomer.setId(customerAccountDTO.getId());
            updateCustomer.setUsername(customerAccountDTO.getUsername());
            updateCustomer.setPassword(customerAccountDTO.getPassword());
            updateCustomer.setCustomerId(customerAccountDTO.getCustomerId());
            return mapToDto(customerAccountRepository.save(updateCustomer));
        }
        return null;
    }

    @Override
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
       String checkToken =  passwordResetTokenService.validatePasswordResetToken(token);
        if(!checkToken.equals("Valid")){
            throw new WmAPIException(HttpStatus.BAD_REQUEST,checkToken);
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPass = passwordEncoder.encode(newPass);
        CustomerAccounts accounts = mapToEntity(accountDTO);
        accounts.setPassword(encodedPass);
        passwordResetTokenService.delete(token);
        customerAccountRepository.save(accounts);
        return "Your Password Has Been Updated";
    }

    @Override
    public String processForgotPassword(String email) {
       CustomerAccountDTO customerAccountDTO = findByEmail(email);
       String tokenCreated = passwordResetTokenService.create(customerAccountDTO.getCustomerId());

       String recipient = email;
       String subject = "Reset Password - WM RESTAURANT" ;
       String link = SD.DOMAIN_APP_CLIENT + "customer/changePassword?token=" + tokenCreated;
        String content =
                "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
       try{
           sender.sendEmail(recipient,subject,content);
           return "The Email Has Been Sent";
       }catch(Exception e){
           return "Unable to send email!";
       }
    }


    public CustomerAccountDTO mapToDto(CustomerAccounts customerAccounts){
        return modelMapper.map(customerAccounts,CustomerAccountDTO.class);
    }

    public CustomerAccounts mapToEntity(CustomerAccountDTO customerAccountDTO){
        return modelMapper.map(customerAccountDTO,CustomerAccounts.class);
    }


}