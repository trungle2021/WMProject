package com.springboot.wmproject.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
public class CustomerDTO {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String gender;
    private Set<BookingDTO> bookings;
    private Set<OrderDTO> orders;

}
