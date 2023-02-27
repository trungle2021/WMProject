package com.springboot.wmproject.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAccountDTO {
    private int id;
    private String phone;
    private String pin;
    private Integer customerId;
}
