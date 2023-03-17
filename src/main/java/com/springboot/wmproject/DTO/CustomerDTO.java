package com.springboot.wmproject.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.FetchType;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private int id;
    private String firstname;
    private String lastname;
    private String address;
    private String phone;
    private String gender;
    private String email;
    @Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    private String avatar;
    private String avatarFromDB;
    public String getAvatar() {
        return this.avatar != null ? this.avatar : this.avatarFromDB;
    }

//    private Set<OrderDTO> orders = new HashSet<>();

}
