package com.springboot.wmproject;

import com.springboot.wmproject.DTO.BookingDTO;
import com.springboot.wmproject.DTO.CustomerDTO;
import com.springboot.wmproject.entities.Booking;
import com.springboot.wmproject.entities.Customers;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication

public class WmprojectApplication {
    @Bean
    public ModelMapper modelMapper(){

        return new ModelMapper();
    }

    public static void main(String[] args) {
        PasswordEncoder pw = new BCryptPasswordEncoder();
        System.out.println("Admin: "+pw.encode("admin"));
        System.out.println("trungle: "+pw.encode("trungle"));
        SpringApplication.run(WmprojectApplication.class, args);
    }

}
