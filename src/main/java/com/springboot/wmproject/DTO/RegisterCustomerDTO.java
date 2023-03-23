package com.springboot.wmproject.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCustomerDTO {
    private int id;


    private String first_name;

    private String last_name;

    private String address;

    private String phone;

    private String gender;

    private String email;
    private String avatar;

    private String username;

    private String password;
    private Integer customerId;
}
