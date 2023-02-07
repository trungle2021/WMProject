package com.springboot.wmproject.DTO;

import com.springboot.wmproject.entities.CustomerAccounts;
import com.springboot.wmproject.entities.Customers;
import com.springboot.wmproject.entities.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Data
public class CustomerDTO {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String gender;

    private Set<BookingDTO> bookingDTOS;

}
